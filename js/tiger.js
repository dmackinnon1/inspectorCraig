'user strict'
// The tiger object tracks the status of the selected puzzle
let tiger = {}; tiger.puzzles = []; tiger.activeSet = []; tiger.selected = null; //the puzzle selected for the user tiger.answered = false; tiger.version = 1;
// UI must initialize display elements
let display = {};
display.versionDescription = null;
display.puzzleTitle = null;
display.puzzleIntro = null;
display.puzzleDescription = null;
display.doorDisplay = null;
display.disabled = false;
display.solutionDisplay = null;


//--- functions for displaying puzzle elements ----
function formatPuzzle(pc) {
	let pi = display.puzzleIntro;
	let pd = display.puzzleDescription;
	let dd =  display.doorDisplay;
	let pt = display.puzzleTitle;
	let ld = display.latexDisplay;
	pt.innerHTML = pc.puzzleTitle()
	pd.innerHTML = pc.cluesDisplay();
	ld.innerHTML = pc.cluesLatex();
	dd.innerHTML = pc.doorDisplay();
	pi.innerHTML = pc.puzzleIntro();
	let solD = display.solutionDisplay;
	solD.innerHTML ="";
}

function solvePuzzle(){
	display.disabled = true;
	let solD = display.solutionDisplay;
	solD.innerHTML = tiger.selected.solutionDisplay();
} 

function puzzleReset(url = null) {
	let id = null;
	if (url != null){
		id = getQueryParameter(url, 'id');
	}
	if  (tiger.activeSet.length == 0) {
	 tiger.activeSet = tiger.puzzles;
	}
	let p = null;
	if (id != null) {
		p = getPuzzleWithId(id);
 	}
 	if (p == null){
 		p = randomElement(tiger.activeSet);	
 	}

 	tiger.selected = new PuzzleController(p);
 	tiger.activeSet = removeElement(tiger.activeSet,p);
	formatPuzzle(tiger.selected);	
 	tiger.answered = false;
	display.disabled = false;
	updateAllDoorButtons();
}

function getPuzzleWithId(id) {
	let x = null;
	for (x in tiger.activeSet){
		let p = tiger.activeSet[x];
		 if (p.description == "Puzzle " + id){
		 	return p;
		 }
	}
	console.log('No puzzle with provided id was found in active set: '+ id)
	return null;
}	

/*
A puzzle has json format like the following:
{
  "door1_clue": "this room has treasure",
  "door2_clue": "at least one room has treasure",
  "door1_propositions": [
    "(D1 -> D1)",
    "(-D1 -> -D1)"
  ],
  "door2_propositions": [
    "(D2 -> <-D1, -D2>)",
    "(-D2 -> [D1, D2])"
  ],
  "solution": [
    "D1",
    "-D2"
  ],
  "solution_text": "Door 1 has treasure. Door 2 has a tiger.",
  "description": "Puzzle 1"
}	
*/
class PuzzleController {
	
	constructor(puzzle){
		this.puzzle = puzzle;
		this.inscriptions = [];
		this.tigerList = [];
		this.treasureList = [];
		this.doorList = ['D1','D2'];
		this.unknownList = this.doorList;
		this.clues=[];
		this.init();
	}
	
	init(){
		this.initInscriptions();
	}

	initInscriptions() {
		for(var i in this.puzzle.door1_propositions){
			let parser = new Parser(this.puzzle.door1_propositions[i],this.unknownList);
			this.clues.push(parser.parse());
		}
		for(var i in this.puzzle.door2_propositions){
			let parser = new Parser(this.puzzle.door2_propositions[i],this.unknownList);
			this.clues.push(parser.parse());
		}
	}

	puzzleIntro() {
		let intro = "If Door 1 leads to treasure, its inscriptions are true, otherwise they are false."
		intro += " If Door 2 leads to a tiger, its inscriptions are true, otherwise they are false. ";
		return intro;

	}
	puzzleTitle() {
		return this.puzzle.description;
	}

	cluesDisplay() {
		let c = "<ul><br>";		
		c += "<li>Door 1 says: " + completeSentence(this.puzzle.door1_clue) + "</li>";			
		c += "<li>Door 2 says: " + completeSentence(this.puzzle.door2_clue) + "</li>";			
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

	doorDisplay() {
		let s = "<div> <table>";
		s += "<tr><th>Door</th><th>treasure</th><th>tiger</th><th>unknown</th></tr>"
		s += new DoorController("Door 1", "D1").display();
		s += new DoorController("Door 2", "D2").display();		
		s += "</table></div>";
		return s;
	}

	solutionDisplay(){
		let statements = [];
		if (this.tigerList.length > 1) {
			statements.push(" " + prettyPrintList(this.tigerList) + " lead to a tiger");
		} else if (this.tigerList.length == 1) {
			statements.push(" " + prettyPrintList(this.tigerList) + " leads to a tiger");
		}
		if (this.treasureList.length > 1) {
			statements.push(" " + prettyPrintList(this.treasureList) + " lead to treasure");
		} else if (this.treasureList.length == 1) {
			statements.push(" " + prettyPrintList(this.treasureList) + " leads to treasure");
		}
		if (this.unknownList.length > 0) {
			statements.push(" we can't be sure about " + prettyPrintList(this.unknownList));
		} 
		let s =  completeSentence("You said" + prettyPrintList(statements));
		s += "<br>"		
		if (this.verifyTreasure()&&this.verifyTiger()&&this.verifyUnknown()){
			s += "<em>You were right.</em>"
		} else {
			s += "You were <em>wrong</em>. The correct solution is: "
			s +=  this.puzzle.solution_text;
		}
		return s;		
	}

	verifyTreasure() {
		let success = true;
		for (var i in this.treasureList){
			success = success && arrayContains(this.puzzle.solution,this.treasureList[i]);
		}
		return success;
	}

	verifyTiger() {
		let success = true;
		for (var i in this.tigerList){
			success = success && arrayContains(this.puzzle.solution,"-"+this.tigerList[i]);
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

}

class DoorController {
	constructor(name, code) {
		this.name = name;
		this.code = code;
	}
	display() {
		var txt = "glyphicon glyphicon-unchecked";
		var btn = "<tr><td><span class='prop-column'>" + this.name + "</span></td>";
		btn +=  "<td><button type='button' id='treasure_"+ this.code + "' class='btn btn-secondary', onclick='selectTreasure(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn +=  "<td><button type='button' id='tiger_"+ this.code + "' class='btn btn-secondary', onclick='selectTiger(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn +=  "<td><button type='button' id='unknown_"+ this.code + "' class='btn btn-secondary', onclick='selectUnknown(event)'>";
		btn += "<span class='glypicon " + txt + " lrg-font'></span>"
		btn += "</button></td>";
		btn += "</tr>";
		return btn;		
	}	
}

function printLists() {
	console.log("--------------------------");
	console.log("treasure: " + tiger.selected.treasureList);
	console.log("tiger: " + tiger.selected.tigerList);
	console.log("unknown: " +  tiger.selected.unknownList);
	
}

function selectTreasure(event) {
	if (display.disabled) return;
	let treasureList = tiger.selected.treasureList;
	let tigerList = tiger.selected.tigerList;
	let unknownList = tiger.selected.unknownList;
	let id = event.currentTarget.id;
	let door = id.substring(id.indexOf('_')+1, id.length);	
 	tiger.selected.treasureList = addOrRemove(treasureList, door);
 	tiger.selected.tigerList = removeElement(tigerList, door);
 	tiger.selected.unknownList = removeElement(unknownList, door);
	updateDoorButtons(door);
};

function selectTiger(event) {
	if (display.disabled) return;
	let treasureList = tiger.selected.treasureList;
	let tigerList = tiger.selected.tigerList;
	let unknownList = tiger.selected.unknownList;
	let id = event.currentTarget.id;
	let door = id.substring(id.indexOf('_')+1, id.length);
 	tiger.selected.tigerList = addOrRemove(tigerList, door);
 	tiger.selected.treasureList = removeElement(treasureList, door);
 	tiger.selected.unknownList = removeElement(unknownList, door);
	updateDoorButtons(door);
};

function selectUnknown(event) {
	if (display.disabled) return;
	let treasureList = tiger.selected.treasureList;
	let tigerList = tiger.selected.tigerList;
	let unknownList = tiger.selected.unknownList;
	let id = event.currentTarget.id;
	let door = id.substring(id.indexOf('_')+1, id.length);	
 	tiger.selected.tigerList = removeElement(tigerList, door);
 	tiger.selected.treasureList = removeElement(treasureList, door);
 	tiger.selected.unknownList = addOrRemove(unknownList, door);
	updateDoorButtons(door);
};

function updateAllDoorButtons(){
	updateDoorButtons("D1");
	updateDoorButtons("D2")	
}

function updateDoorButtons(door){
	let treasureList = tiger.selected.treasureList;
	let tigerList = tiger.selected.tigerList;
	let unknownList = tiger.selected.unknownList;

	if (arrayContains(treasureList,door)) {
		$("#treasure_" + door).addClass("btn-primary");
		$("#treasure_" + door).removeClass("btn-secondary");				
		$("#treasure_" + door).html("<span class='glypicon glyphicon glyphicon-ok lrg-font'></span>");		
	} else {
		$("#treasure_" + door).removeClass("btn-primary");	
		$("#treasure_" + door).removeClass("btn-secondary");
		$("#treasure_" + door).addClass("btn-secondary");
		$("#treasure_" + door).html("<span class='glypicon glyphicon glyphicon-unchecked lrg-font'></span>");	
	}
	if (arrayContains(tigerList,door)) {
		$("#tiger_" + door).addClass("btn-primary");		
		$("#tiger_" + door).removeClass("btn-secondary");						
		$("#tiger_" + door).html("<span class='glypicon glyphicon glyphicon-ok lrg-font'></span>");		
	} else {
		$("#tiger_" + door).removeClass("btn-primary");
		$("#tiger_" + door).removeClass("btn-secondary");						
		$("#tiger_" + door).addClass("btn-secondary");									
		$("#tiger_" + door).html("<span class='glypicon glyphicon glyphicon-unchecked lrg-font'></span>");	
	}
	if (arrayContains(unknownList,door)) {
		$("#unknown_" + door).addClass("btn-primary");		
		$("#unknown_" + door).removeClass("btn-secondary");						
		$("#unknown_" + door).html("<span class='glypicon glyphicon glyphicon-ok lrg-font'></span>");			
	} else {
		$("#unknown_" + door).removeClass("btn-primary");
		$("#unknown_" + door).removeClass("btn-secondary");				
		$("#unknown_" + door).addClass("btn-secondary");							
		$("#unknown_" + door).html("<span class='glypicon glyphicon glyphicon-unchecked lrg-font'></span>");	
	}
	printLists();
}
