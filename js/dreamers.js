"use strict"
/*
 * Isle of Dreams puzzle generator
 *
 */

// The dreamer object tracks the status of the selected puzzle
let dreamer = {}; dreamer.puzzles = []; dreamer.activeSet = []; dreamer.selected = null; //the puzzle selected for the user dreamer.answered = false; dreamer.version = 1;
// UI must initialize display elements
let display = {};
display.versionDescription = null;
display.puzzleTitle = null;
display.puzzleIntro = null;
display.puzzleDescription = null;
display.answerDisplay = null;
display.disabled = false;
display.solutionDisplay = null;


//--- functions for displaying puzzle elements ----
function formatPuzzle(pc) {
	let pi = display.puzzleIntro;
	let pd = display.puzzleDescription;
	let td =  display.thoughtDisplay;
	let pt = display.puzzleTitle;
	let ld = display.latexDisplay;
	let ad = display.answerDisplay;
	pt.innerHTML = pc.puzzleTitle()
	pd.innerHTML = pc.thoughtDisplay();
	//ld.innerHTML = pc.cluesLatex();
	ad.innerHTML = pc.answerDisplay();
	pi.innerHTML = pc.puzzleIntro();
	let solD = display.solutionDisplay;
	solD.innerHTML ="";
}

function solvePuzzle(){
	display.disabled = true;
	let solD = display.solutionDisplay;
	solD.innerHTML = dreamer.selected.solutionDisplay();
} 

function puzzleReset() {
	if  (dreamer.activeSet.length == 0) {
	 dreamer.activeSet = dreamer.puzzles;
	}
	let p = randomElement(dreamer.activeSet);
 	dreamer.selected = new PuzzleController(p);
 	dreamer.activeSet = removeElement(dreamer.activeSet,p);
	formatPuzzle(dreamer.selected);	
 	dreamer.answered = false;
	display.disabled = false;
	updateAllDreamerButtons();
}


/*
A puzzle has json format like the following:
{
  "description": "Problem 3",
  "is_consistent": "true",
  "islanderA_statement1": "I am awake.",
  "islanderA_statement2": "B is awake.",
  "islanderB_statement1": "I am awake.",
  "islanderB_statement2": "A is diurnal.",
  "solution_text": "A is awake. B is diurnal. A is diurnal. B is awake.",
  "all_phrases": [
    "(<Aa, Ad> -> Aa)",
    "(<-Aa, Ad> -> -Aa)"
    ...
  ],
  "solution": [
    "Aa",
    "Bd",
    "Ad",
    "Ba"
  ]
}
*/
class PuzzleController {
	
	constructor(puzzle){
		this.puzzle = puzzle;
		this.a1 = null;
		this.a2 = null;
		this.b1 = null;
		this.b2 = null;
		this.dreamerList = ["A", "B"];
		this.unknownTypeList = this.dreamerList;
		this.unknownStateList = this.dreamerList;
		this.nocturnalList = [];
		this.diurnalList = [];
		this.awakeList = [];
		this.asleepList = [];
		this.impossibleList = [];
		
		this.phrases=[];
		this.init();
	}
	
	init(){
		this.initThoughts();
	}

	initThoughts() {
		for(var i in this.puzzle.all_phrases){
			let parser = new Parser(this.puzzle.all_phrases,['Ad','Aa','Bd','Ba']);
			this.phrases.push(parser.parse());
		}
		this.a1 = this.puzzle.islanderA_statement1;
		this.a2 = this.puzzle.islanderA_statement2;
		this.b1 = this.puzzle.islanderB_statement1;
		this.b2 = this.puzzle.islanderB_statement2;
	}

	puzzleIntro() {
		let intro = "On the Isle of Dreams, all islanders are either <em>diurnal</em> or <em>nocturnal</em>.";
		intro += " Diurnal islanders think true thoughts when they are awake, and false ones when asleep.";
		intro += " Nocturnal islanders think true thoughts when they are asleep, and false ones when they are awake"; 
		return intro;

	}
	puzzleTitle() {
		return this.puzzle.description;
	}

	thoughtDisplay() {
		let c = "<ul><br>";
		
		c += "<li>Islander <strong>A</strong> has two distinct thoughts at the same moment: <strong>" + this.a1 + " " + this.a2  + "</strong></li>";			
		c += "<li>At the same moment, islander <strong>B</strong> has these distinct thoughts:  <strong>" + this.b1 +" " + this.b2 + "</strong></li>";			
	
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

	answerDisplay() {
		let s = "<div> <table>";
		s += "<tr><th>Islander</th><th>diurnal</th><th>nocturnal</th><th>unknown</th></tr>"
		s += new TypeController("Islander A", "A").display();
		s += new TypeController("Islander B", "B").display();		
		s += "</table></div>";
		s += "<div> <table><br>";		
		s += "<tr><th>Islander</th><th>awake</th><th>asleep</th><th>unknown</th></tr>"
		s += new StateController("Islander A", "A").display();
		s += new StateController("Islander B", "B").display();		
		s += "</table></div>";
		s += "<br>" + new ImpossibleController().display();
		return s;
	}

	statementFromList(list, word, statements){
		if (list.length > 1) {
				statements.push(" both" + prettyPrintList(list) + " are " + word);
		} else if (list.length == 1) {
				statements.push(" " + prettyPrintList(list) + " is "+ word);
		}
		return statements;
	}

	solutionDisplay(){
		let statements = [];
		if (this.impossibleList.length >0) {
			statements.push("these thoughts are impossible");
		} else {
			this.statementFromList(this.diurnalList, "diurnal", statements);
			this.statementFromList(this.nocturnalList, "nocturnal", statements);
			this.statementFromList(this.awakeList, "awake", statements);
			this.statementFromList(this.asleepList, "asleep", statements);	
		}
		/*
		if (this.unknownList.length > 0) {
			statements.push(" we can't be sure about " + prettyPrintList(this.unknownList));
		} 
		*/
		let s =  completeSentence("You said" + prettyPrintList(statements));
		s += "<br>"		
		if (this.verifyAll()){
			s += "<em>You were right.</em>"
		} else {
			s += "You were <em>wrong</em>. The correct solution is: "
			s +=  this.puzzle.solution_text;
			if (this.puzzle.is_consistent === "false"){
				s += "These are impossible thoughts, you must be dreaming."
			}
		}
		return s;		
	}

	verifyAll() {
		return this.verifyImpossible()
			&& this.verifyDiurnal()
			&& this.verifyNocturnal()
			&& this.verifyAwake()
			&& this.verifyAsleep();
	}

	verifyImpossible(){
		if (this.impossibleList.length > 0){
			return this.puzzle.is_consistent === "false";
		} else {
			return this.puzzle.is_consistent === "true";
		}
	}

	verifyDiurnal() {
		let success = true;
		for (var i in this.diurnalList){
			success = success && arrayContains(this.puzzle.solution,this.diurnalList[i] + "d");
		}
		return success;
	}

	verifyNocturnal() {
		let success = true;
		for (var i in this.nocturnalList){
			success = success && arrayContains(this.puzzle.solution,"-"+this.nocturnalList[i] +"d");
		}
		return success;
	}

	verifyAwake() {
		let success = true;
		for (var i in this.awakeList){
			success = success && arrayContains(this.puzzle.solution,this.awakeList[i] + "a");
		}
		return success;
	}

	verifyAsleep() {
		let success = true;
		for (var i in this.asleepList){
			success = success && arrayContains(this.puzzle.solution,"-"+this.asleepList[i] +"a");
		}
		return success;
	} 

}

class TypeController {
	constructor(name, code) {
		this.name = name;
		this.code = code;
	}
	display() {
		var txt = "glyphicon glyphicon-unchecked";
		var btn = "<tr><td><span class='prop-column'>" + this.name + "</span></td>";
		btn +=  "<td><button type='button' id='diurnal_"+ this.code + "' class='btn btn-secondary', onclick='selectDiurnal(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn +=  "<td><button type='button' id='nocturnal_"+ this.code + "' class='btn btn-secondary', onclick='selectNocturnal(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn +=  "<td><button type='button' id='type_unknown_"+ this.code + "' class='btn btn-secondary', onclick='selectTypeUnknown(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn += "</tr>";
		return btn;		
	}	
}


class StateController {
	constructor(name, code) {
		this.name = name;
		this.code = code;
	}
	display() {
		var txt = "glyphicon glyphicon-unchecked";
		var btn = "<tr><td><span class='prop-column'>" + this.name + "</span></td>";
		btn +=  "<td><button type='button' id='awake_"+ this.code + "' class='btn btn-secondary', onclick='selectAwake(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn +=  "<td><button type='button' id='asleep_"+ this.code + "' class='btn btn-secondary', onclick='selectAsleep(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn +=  "<td><button type='button' id='state_unknown_"+ this.code + "' class='btn btn-secondary', onclick='selectStateUnknown(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn += "</tr>";
		return btn;		
	}	
}

class ImpossibleController {
	display() {
		var txt = "glyphicon glyphicon-unchecked";
		var btn ="<table><tr><td><strong>Impossible!</strong> These thoughts are unthinkable. </td>";
		btn  += "<td><button type='button' id='impossible_A' class='btn btn-secondary', onclick='selectImpossible(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td></tr></table>";
		return btn;		
	}	
}


function printLists() {
	console.log("--------------------------");
	console.log("dreamers: " + dreamer.selected.dreamerList);
	console.log("diurnals: " + dreamer.selected.diurnalList);
	console.log("nocturnals: " +  dreamer.selected.nocturnalList);
	console.log("awake: " + dreamer.selected.awakeList);
	console.log("asleep: " +  dreamer.selected.asleepList);
		
}

function selectDiurnal(event) {
	if (display.disabled) return;
	let id = event.currentTarget.id;
	let islander = id.substring(id.indexOf('_')+1, id.length);	
 	dreamer.selected.diurnalList = addOrRemove(dreamer.selected.diurnalList, islander);
 	dreamer.selected.nocturnalList = removeElement(dreamer.selected.nocturnalList, islander);
 	dreamer.selected.unknownTypeList = removeElement(dreamer.selected.unknownTypeList, islander); 	
	updateDreamerButtons(islander);
};

function selectNocturnal(event) {
	if (display.disabled) return;
	let id = event.currentTarget.id;
	let islander = id.substring(id.indexOf('_')+1, id.length);	
 	dreamer.selected.nocturnalList = addOrRemove(dreamer.selected.nocturnalList, islander);
 	dreamer.selected.diurnalList = removeElement(dreamer.selected.diurnalList, islander);
 	dreamer.selected.unknownTypeList = removeElement(dreamer.selected.unknownTypeList, islander); 	
	updateDreamerButtons(islander);
};

function selectTypeUnknown(event) {
	if (display.disabled) return;
	let id = event.currentTarget.id;
	let islander = id.substring(id.indexOf('_')+1, id.length);	
 	dreamer.selected.nocturnalList = removeElement(dreamer.selected.nocturnalList, islander);
 	dreamer.selected.diurnalList = removeElement(dreamer.selected.diurnalList, islander);
 	dreamer.selected.unknownTypeList = addOrRemove(dreamer.selected.unknownTypeList, islander); 	
	updateDreamerButtons(islander);
};

function selectAwake(event) {
	if (display.disabled) return;
	let id = event.currentTarget.id;
	let islander = id.substring(id.indexOf('_')+1, id.length);	
 	dreamer.selected.awakeList = addOrRemove(dreamer.selected.awakeList, islander);
 	dreamer.selected.asleepList = removeElement(dreamer.selected.asleepList, islander);
 	dreamer.selected.unknownStateList = removeElement(dreamer.selected.unknownStateList, islander); 	
	updateDreamerButtons(islander);
};

function selectAsleep(event) {
	if (display.disabled) return;
	let id = event.currentTarget.id;
	let islander = id.substring(id.indexOf('_')+1, id.length);	
 	dreamer.selected.awakeList = removeElement(dreamer.selected.awakeList, islander);
 	dreamer.selected.asleepList = addOrRemove(dreamer.selected.asleepList, islander);
 	dreamer.selected.unknownStateList = removeElement(dreamer.selected.unknownStateList, islander); 	
	updateDreamerButtons(islander);
};

function selectStateUnknown(event) {
	if (display.disabled) return;
	let id = event.currentTarget.id;
	let islander = id.substring(id.indexOf('_')+1, id.length);	
 	dreamer.selected.awakeList = removeElement(dreamer.selected.awakeList, islander);
 	dreamer.selected.asleepList = removeElement(dreamer.selected.asleepList, islander);
 	dreamer.selected.unknownStateList = addOrRemove(dreamer.selected.unknownStateList, islander); 	
	updateDreamerButtons(islander);
};

function selectImpossible(event) {
	if (display.disabled) return;	
 	dreamer.selected.impossibleList = addOrRemove(dreamer.selected.impossibleList, "A"); 	
	updateDreamerButtons("A");
};


function updateAllDreamerButtons(){
	updateDreamerButtons("A");
	updateDreamerButtons("B")	
}

function updateDreamerButtons(islander){
	updateDreamerButton("#awake_", dreamer.selected.awakeList, islander);
	updateDreamerButton("#asleep_", dreamer.selected.asleepList, islander);
	updateDreamerButton("#state_unknown_", dreamer.selected.unknownStateList, islander);
	updateDreamerButton("#diurnal_", dreamer.selected.diurnalList, islander);
	updateDreamerButton("#nocturnal_", dreamer.selected.nocturnalList, islander);
	updateDreamerButton("#type_unknown_", dreamer.selected.unknownTypeList, islander);
	updateDreamerButton("#impossible_", dreamer.selected.impossibleList, islander);

}

function updateDreamerButton(prefix, list, islander){
	if (arrayContains(list,islander)) {
		$(prefix + islander).addClass("btn-primary");
		$(prefix + islander).removeClass("btn-secondary");				
		$(prefix + islander).html("<span class='glypicon glyphicon glyphicon-ok lrg-font'></span>");		
	} else {
		$(prefix + islander).removeClass("btn-primary");	
		$(prefix + islander).removeClass("btn-secondary");
		$(prefix + islander).addClass("btn-secondary");
		$(prefix + islander).html("<span class='glypicon glyphicon glyphicon-unchecked lrg-font'></span>");	
	}
	printLists();
}
