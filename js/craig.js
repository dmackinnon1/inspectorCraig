
// The craig object tracks the status of the selected puzzle
let craig = {};
craig.puzzles = [];
craig.activeSet = [];
craig.selected = null; //the puzzle selected for the user
craig.answered = false;
craig.version = 1;
// UI must initialize display elements
let display = {};
display.versionDescription = null;
display.puzzleTitle = null;
display.puzzleIntro = null;
display.puzzleDescription = null;
display.suspectDisplay = null;
display.disabled = false;
display.solutionDisplay = null;

//--- functions for displaying puzzle elements ----
function formatPuzzle(pc) {
	let pi = display.puzzleIntro;
	let pd = display.puzzleDescription;
	let sd =  display.suspectDisplay;
	let pt = display.puzzleTitle;
	let ld = display.latexDisplay;
	pt.innerHTML = pc.puzzleTitle()
	pd.innerHTML = pc.cluesDisplay();
	ld.innerHTML = pc.cluesLatex();
	sd.innerHTML = pc.suspectsDisplay();
	pi.innerHTML = pc.puzzleIntro();
	let solD = display.solutionDisplay;
	solD.innerHTML ="";
}

function solvePuzzle(){
	display.disabled = true;
	let solD = display.solutionDisplay;
	solD.innerHTML = craig.selected.solutionDisplay();
} 

function puzzleReset() {
	if (craig.activeSet.length == 0) {
		craig.activeSet = craig.puzzles;
	}
	let p = randomElement(craig.activeSet);
	craig.selected = new PuzzleController(p);
	craig.activeSet = removeElement(craig.activeSet,p);
	formatPuzzle(craig.selected);	
	craig.answered = false;
	display.disabled = false;
	updateAllSuspectButtons();
}

let intros = [
	"An enormous amount of loot had been stolen from a store. ",
	"A robbery occurred in London. ",
	"Mr McGreggor, a London shopkeeper, called Scotland Yard to report a robbery. ",
	"A daring jewel heist occurred in broad daylight. ",
	"Scotland Yard has been summoned to investigate a break-in at an art gallery. ",
	"Inspector Craig was called in to investigate a mysterious break in at a manor house. ",
	"A bank in London's financial district was robbed. ",
	"A shop owner found her safe broken into when arriving to work one morning. ",
	"Funds raised for a charity were found to be stolen. ",
	"A string of break-ins were reported in a London neighborhood. ",
	"Luxury cars have been stolen from several dealers in the same area. ",
	"Shipments of expensive items have been going 'missing' from the docs. ",
	"Several thefts had been brought to Inspector Craig's attention. ",
	"A case of a suspected car theft ring was being investigated. ",
	"A gang of thieves had been harassing neighborhood merchants. "
];

/*
A puzzle has json format like the following:
{
	"propositions": ["A", "B", "C"], 
	"premises": ["(B -> C)", "(C -> -A)", "[A, B, C]", "(A -> [B, C])"], 
	"solution": ["-A", "C"], "description": "Modification of problem 71, WITNOTB"
}	
*/
class PuzzleController {
	
	constructor(puzzle){
		this.puzzle = puzzle;
		this.clues = [];
		this.innocentList = [];
		this.guiltyList = [];
		this.unknownList = this.puzzle.propositions;
		this.init();
	}
	
	init(){
		this.initClues();
	}

	initClues() {
		for(var i in this.puzzle.premises){
			let parser = new Parser(this.puzzle.premises[i],this.puzzle.propositions);
			this.clues.push(parser.parse());
		}
	}

	puzzleIntro() {
		let intro = randomElement(intros);
		intro += "The notorious criminals, " + prettyPrintList(this.puzzle.propositions);
		intro += " were brought in for questioning. ";
		intro += "The following evidence was established.";
		return intro;

	}
	puzzleTitle() {
		return "Case " + this.puzzle.description;
	}

	cluesDisplay() {
		let c = "<ul><br>";
		for (var i in this.clues){
			c += "<li>" + completeSentence(this.clues[i].description()) + "</li>";			
		}
		c+="<ul>";
		return c;
	}

	cluesLatex() {
		let c = "<ul>";
		for (var i in this.clues){
			c += "<li> \\(" +this.clues[i].latex() + "\\)</li>";			
		}
		c+="<ul>";
		return c;
	}

	suspectsDisplay() {
		let s = "<div> <table>";
		s += "<tr><th>Suspect</th><th>guilty</th><th>innocent</th><th>unknown</th></tr>"
		for (var i in this.puzzle.propositions) {
			let suspect = this.puzzle.propositions[i]; 
			s += new SuspectController(suspect).display();
		}
		s += "</table></div>";
		return s;
	}

	solutionDisplay(){
		let statements = [];
		if (this.innocentList.length > 1) {
			statements.push(" " + prettyPrintList(this.innocentList) + " are innocent");
		} else if (this.innocentList.length == 1) {
			statements.push(" " + prettyPrintList(this.innocentList) + " is innocent");
		}
		if (this.guiltyList.length > 1) {
			statements.push(" " + prettyPrintList(this.guiltyList) + " are guilty");
		} else if (this.guiltyList.length == 1) {
			statements.push(" " + prettyPrintList(this.guiltyList) + " is guilty");
		}
		if (this.unknownList.length > 0) {
			statements.push(" we can't be sure about " + prettyPrintList(this.unknownList));
		} 
		let s =  completeSentence("You said" + prettyPrintList(statements));
		s += "<br>"		
		if (this.verifyGuilty()&&this.verifyInnocent()&&this.verifyUnknown()){
			s += "<em>You were right.</em>"
		} else {
			s += "You were <em>wrong</em>. The correct solution is: "
			s +=  this.correctSolution();
		}
		return s;		
	}

	verifyGuilty() {
		let success = true;
		for (var i in this.guiltyList){
			success = success && arrayContains(this.puzzle.solution,this.guiltyList[i]);
		}
		return success;
	}

	verifyInnocent() {
		let success = true;
		for (var i in this.innocentList){
			success = success && arrayContains(this.puzzle.solution,"-"+this.innocentList[i]);
		}
		return success;
	}

	verifyUnknown() {
		let success = true;
		for (var i in this.unknownList){
			success = success && !arrayContains(this.puzzle.solution,"-"+this.unknownList[i]);
			success = success && !arrayContains(this.puzzle.solution, this.unknownList[i]);
		}
		return success;
	}

	correctSolution(){
		let s = "<br><ul>";
		for(var i in this.puzzle.propositions){
			let p = this.puzzle.propositions[i];
			s += "<li>" + p +": ";
			if (arrayContains(this.puzzle.solution, p)){
				s += " guilty</li>";
			} else if (arrayContains(this.puzzle.solution, "-" +p)){
				s += " innocent</li>";
			} else {
				s += " unknown</li>";
			}
		}
		s += "</ul>";
		return s;
	}
}

class SuspectController {
	constructor(suspect) {
		this.suspect = suspect;
	}
	display() {
		var txt = "glyphicon glyphicon-unchecked";
		var btn = "<tr><td><span class='prop-column'>" + this.suspect + "</span></td>";
		btn +=  "<td><button type='button' id='g_"+ this.suspect + "' class='btn btn-secondary', onclick='selectGuilty(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn +=  "<td><button type='button' id='i_"+ this.suspect + "' class='btn btn-secondary', onclick='selectInnocent(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn +=  "<td><button type='button' id='u_"+ this.suspect + "' class='btn btn-secondary', onclick='selectUnknown(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn += "</tr>";
		return btn;		
	}	
}

function printLists() {
	console.log("guilty: " + craig.selected.guiltyList);
	console.log("innocent: " + craig.selected.innocentList);
	console.log("unknown: " +  craig.selected.unknownList);
	
}

function selectGuilty(event) {
	if (display.disabled) return;
	let guiltyList = craig.selected.guiltyList;
	let innocentList = craig.selected.innocentList;
	let unknownList = craig.selected.unknownList;
	let id = event.currentTarget.id;
	let suspect = id.substring(id.indexOf('_')+1, id.length);	
	craig.selected.guiltyList = addOrRemove(guiltyList, suspect);
	craig.selected.innocentList = removeElement(innocentList, suspect);
	craig.selected.unknownList = removeElement(unknownList, suspect);
	updateSuspectButtons(suspect);
};

function selectInnocent(event) {
	if (display.disabled) return;
	let guiltyList = craig.selected.guiltyList;
	let innocentList = craig.selected.innocentList;
	let unknownList = craig.selected.unknownList;
	let id = event.currentTarget.id;
	let suspect = id.substring(id.indexOf('_')+1, id.length);	
	craig.selected.innocentList = addOrRemove(innocentList, suspect);
	craig.selected.guiltyList = removeElement(guiltyList, suspect);
	craig.selected.unknownList = removeElement(unknownList, suspect);
	updateSuspectButtons(suspect);
};

function selectUnknown(event) {
	if (display.disabled) return;
	let guiltyList = craig.selected.guiltyList;
	let innocentList = craig.selected.innocentList;
	let unknownList = craig.selected.unknownList;
	let id = event.currentTarget.id;
	let suspect = id.substring(id.indexOf('_')+1, id.length);	
	craig.selected.innocentList = removeElement(innocentList, suspect);
	craig.selected.guiltyList = removeElement(guiltyList, suspect);
	craig.selected.unknownList = addOrRemove(unknownList, suspect);
	updateSuspectButtons(suspect);
};

function updateAllSuspectButtons(){
	let props = craig.selected.puzzle.propositions;
	for (var i in props){
		updateSuspectButtons(props[i]);
	}
}

function updateSuspectButtons(suspect){
	let guiltyList = craig.selected.guiltyList;
	let innocentList = craig.selected.innocentList;
	let unknownList = craig.selected.unknownList;

	if (arrayContains(guiltyList,suspect)) {
		$("#g_" + suspect).addClass("btn-primary");
		$("#g_" + suspect).removeClass("btn-secondary");				
		$("#g_" + suspect).html("<span class='glypicon glyphicon glyphicon-ok lrg-font'></span>");		
	} else {
		$("#g_" + suspect).removeClass("btn-primary");	
		$("#g_" + suspect).removeClass("btn-secondary");
		$("#g_" + suspect).addClass("btn-secondary");
		$("#g_" + suspect).html("<span class='glypicon glyphicon glyphicon-unchecked lrg-font'></span>");	
	}
	if (arrayContains(innocentList,suspect)) {
		$("#i_" + suspect).addClass("btn-primary");		
		$("#i_" + suspect).removeClass("btn-secondary");						
		$("#i_" + suspect).html("<span class='glypicon glyphicon glyphicon-ok lrg-font'></span>");		
	} else {
		$("#i_" + suspect).removeClass("btn-primary");
		$("#i_" + suspect).removeClass("btn-secondary");						
		$("#i_" + suspect).addClass("btn-secondary");									
		$("#i_" + suspect).html("<span class='glypicon glyphicon glyphicon-unchecked lrg-font'></span>");	
	}
	if (arrayContains(unknownList,suspect)) {
		$("#u_" + suspect).addClass("btn-primary");		
		$("#u_" + suspect).removeClass("btn-secondary");						
		$("#u_" + suspect).html("<span class='glypicon glyphicon glyphicon-ok lrg-font'></span>");			
	} else {
		$("#u_" + suspect).removeClass("btn-primary");
		$("#u_" + suspect).removeClass("btn-secondary");				
		$("#u_" + suspect).addClass("btn-secondary");							
		$("#u_" + suspect).html("<span class='glypicon glyphicon glyphicon-unchecked lrg-font'></span>");	
	}
	printLists();
}
