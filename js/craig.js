
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
	pt.innerHTML = pc.puzzleTitle()
	pd.innerHTML = pc.cluesDisplay();
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
	craig.selected = new PuzzleController(randomElement(craig.activeSet));
	craig.activeSet = removeElement(craig.selected);
	formatPuzzle(craig.selected);	
	craig.answered = false;
	display.disabled = false;
	updateAllSuspectButtons();
}

let intros = [
	"An enormous amount of loot had been stolen from a store. ",
	"A robbery occured in London. ",
	"Mr McGreggor, a London shopkeeper, phoned Scotland Yard to report a robbery. ",
	"A daring jewel heist occurred in broad daylight. ",
	"Scotland Yard has been summoned to investigate a break in at an art gallery. ",
	"Inspector Craig was called in to investigate a mysterious break in at a manor house. "
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
		let c = "<br><ul>";
		for (var i in this.clues){
			c += "<li>" + completeSentence(this.clues[i].description()) + "</li>";			
		}
		c+="<ul><br>";
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
		var btn = "<tr><td><span class='puzzle-text'>" + this.suspect + "</span></td>";
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

//for parsing puzzle clues
class Token {
	constructor(value, type) {
		this.value = value;
		this.type = type;
	}
	isToken(){
		return true;
	}
	static end() {
		return new Token(null, 'end');
	}
	static beginUnion(){
		return new Token('[', 'beginUnion');
	}
	static endUnion(){
		return new Token(']', 'endUnion');
	}
	static beginIntersection(){
		return new Token('<', 'beginIntersection')
	}
	static endIntersection(){
		return new Token('>', 'endIntersection');
	}
	static implication(){
		return new Token('-->', 'implication');
	}
	static negation(){
		return new Token('-', 'negation');
	}
	static error(value){
		return new Token(value, 'error');
	}
	isProposition() {
		return this.type === 'proposition';		
	}
	isBeginUnion() {
		return this.type === 'beginUnion';
	}
	isEndUnion() {
		return this.type === 'endUnion';
	}
	isBeginIntersection() {
		return this.type === 'beginIntersection';		
	}
	isImplication() {		
		return this.type === 'implication';		
	}
	isEndIntersection() {
		return this.type === 'endIntersection';
	}
	toString() {
		return "(" + this.value + ")." + this.type + " ";
	}
	isError() {
		return this.type === 'error';
	}
	isNegation() {
		return this.type === 'negation';
	}
	isEnd() {
		return this.type == 'end';
	}
}

//models the basic propositions A, -B, etc.
class Proposition {
	constructor(symbol, sign){
		this.symbol = symbol;
		this.sign = sign;
	}	
	isToken(){
		return false;
	}
	isGuilty(){
		return this.sign;
	}
	name(){
		return this.symbol;
	}
	description(){
		if (this.isGuilty()) {
			return this.symbol + " is guilty";
		} else {
			return this.symbol + " is innocent";
		}
	}
	isProposition(){
		return true;
	}
	toString() {
		if (! this.sign) {
			return "-" + this.symbol;
		} else {
			return this.symbol;
		}
	}
}

class Union {
	constructor(){
		this.propositions = []; 
	}
	isToken(){
		return false;
	}
	allGuilty(){
		let allGuilty = true;
		for (var i in this.propositions){
			allGuilty = allGuilty&&this.propositions[i].sign;
		}
		return allGuilty;
	}
	description() {
		let d = "";
		let first = true;
		let count = 1;		
		if (this.allGuilty()){
			d += "at least one of ";
			for (var i in this.propositions){
				if (!first) {
					if (this.propositions.length > 2) {
						d += ", ";
						if (count == this.propositions.length) {
							d += "or ";
						}
					} else {
						d += " or ";
					}
				}
				count ++;
				first = false;
				d += this.propositions[i].symbol;
			}
			d += " is guilty"; 	
		} else {
			for (var i in this.propositions){
				if (!first) {
					if (this.propositions.length > 2) {
						d += ", ";
						if (count == this.propositions.length) {
							d += "or ";
						}
					} else {
						d += " or ";
					}
				}
				count ++;
				first = false;
				d += this.propositions[i].description();
			}
		}
		return d;
	}
	isProposition(){
		return false;
	}
	toString(){
		return "(" + this.propositions + ")";
	}
}

class Intersection {
	constructor(){
		this.propositions = []; 
	}
	isToken(){
		return false;
	}
	isProposition(){
		return false;
	}
	toString(){
		return "<" + this.propositions + ">";
	}
	description() {
		let d = "";
		let first = true;
		let count = 1;
		for (var i in this.propositions){
			if (!first) {
				if (this.propositions.length > 2) {
					d += ", ";
					if (count == this.propositions.length) {
						d += "and ";
					}
				} else {
					d += " and ";
				}
			}
			count ++; 
			first = false;	
			d += this.propositions[i].description();
		}
		return d;
	}
}

class Implication {
	constructor(source, target){
		this.source = source;
		this.target = target;
	}
	isToken(){
		return false;
	}
	isProposition(){
		return false;
	}
	toString(){
		return this.source.toString() + " --> " + this.target.toString();
	}
	simpleImplication(){
		return this.source.isProposition() && this.target.isProposition();
	}
	description() {
		if (this.simpleImplication()){
			if (this.source.isGuilty() && this.target.isGuilty()) {
				return this.source.symbol + " always uses " + this.target.symbol + " as an accomplice";
			} else if (this.source.isGuilty() && !this.target.isGuilty()){
				return this.source.symbol + " never works with " + this.target.symbol;
			}
		}
		return "if " + this.source.description() + " then " + this.target.description();
	}
}

//the parser
class Parser {
	constructor(input, propositions) {		
		this.input = input;
		this.propositions = propositions;
		this.tokens = [];
		this.stack = [];
		this.tokenIndex = 0;
		this.isImplication = false;
		this.implication = null;	
	}
	parse() {
		this.tokenize();
		this.reduce();
		if (this.hasError()) {
			console.log("had some difficulty parsing this " + this.showStack());
		}
		return this.stack[0];
	
	}
	reduce() {
		while(this.hasRemaining()){
			this.buildProposition();
			this.buildNegation();
			this.buildImplication();
			this.startUnionOrIntersection();
			this.finishUnionOrIntersection();
		} 
		if (this.isImplication){
			if (this.stack.length !== 1) {
				this.stack.push(Token.error("-->"));
				console.log("error: consequent too long");
				return;			
			}
			let consequent = this.stack.pop();
			this.implication.target = consequent;
			this.stack.push(this.implication);	
		}	
	}
	buildProposition(){
		while(this.hasRemaining()){
			if (this.currentToken().isError()) {
				this.stack.push(this.currentToken());
				this.tokenIndex ++;
				return;
			} else if (this.currentToken().isProposition()){
				this.stack.push(new Proposition(this.currentToken().value, true));
				this.tokenIndex ++;
			} else {
				break;
			}
		}		
	}
	buildNegation() {
		if (!this.hasRemaining()) return;
		if (this.currentToken().isNegation()){
			this.tokenIndex++;
			if (this.currentToken().isProposition()){
				this.stack.push(new Proposition(this.currentToken().value, false));
				this.tokenIndex ++
			} else {
				this.stack.push(Token.error(this.currentToken().value));
			}
		}
	}
	buildImplication(){
		if (!this.hasRemaining()) return;
		if (!this.currentToken().isImplication()) return;
		if (this.isImplication) {
			this.stack.push(Token.error(this.currentToken().value));
			console.log("error: nested implications not supported");
			this.tokenIndex ++;
			return;
		}
		if (this.stack.length !== 1) {
			this.stack.push(Token.error(this.currentToken().value));
			console.log("error: antecedant too long");
			this.tokenIndex ++;
			return;			
		}	
		this.isImplication = true;
		let antecedant = this.stack.pop();
		this.implication = new Implication(antecedant, null);
		this.tokenIndex ++;		
	}
	startUnionOrIntersection(){
		if (!this.hasRemaining()) return;
		if (this.currentToken().isBeginIntersection() || this.currentToken().isBeginUnion()){
			this.stack.push(this.currentToken());
			this.tokenIndex++;
		}
	}
	finishUnionOrIntersection(){
		if (!this.hasRemaining()) return;
		if (this.currentToken().isEndUnion()){
			this.tokenIndex ++;
			let union = new Union();
			let term = this.stack.pop();
			while(!term.isToken() || !term.isBeginUnion()){
				union.propositions.push(term);
				term = this.stack.pop();
			}
			union.propositions.reverse();
			this.stack.push(union);
		} else if (this.currentToken().isEndIntersection()){
			this.tokenIndex ++;
			let intersection = new Intersection();
			let term = this.stack.pop();
			while(!term.isToken() || !term.isBeginIntersection()){
				intersection.propositions.push(term);
				term = this.stack.pop();
			}
			intersection.propositions.reverse();
			this.stack.push(intersection);
		}
	}
	hasRemaining() {
		return this.tokenIndex < this.tokens.length; 
	}
	currentToken() {
		return this.tokens[this.tokenIndex];
	}
	hasError() {
		let hasError = false;
		for (var i = 0; i < this.stack.length; i++) {
			let current = this.stack[i];
			hasError = current.isToken() && current.isError();
			if (hasError === true) {
				return hasError;
			}
		}
		return hasError;
	}
	showStack() {
		return "" + this.stack;
	}
	tokenize() {
		let ignoreCount = 0;		
		for(var i = 0; i < this.input.length; i++) {
			if (ignoreCount > 0){
				ignoreCount --;
				continue;
			}
			let current = this.input[i];		
			if (current === ' '|| current === ',' || current === '(' || current === ')'){
				continue; //skip
			} else if (current == '[' ) {
				this.tokens.push(Token.beginUnion());
			} else if ( current === ']') {
				this.tokens.push(Token.endUnion())
			} else if ( current === '<') {
				this.tokens.push(Token.beginIntersection());
			} else if ( current === '>') {
				this.tokens.push(Token.endIntersection())
			} else if (this.propositions.includes(current)) {
				this.tokens.push(new Token(current, 'proposition'));
			} else if (current === '-') {
				if (i < this.input.length -1) {
					let peek1 = this.input[i+1];
					if (peek1 === '>'){
						this.tokens.push(Token.implication());
						ignoreCount = 1;
					} else {
						this.tokens.push(Token.negation());	
					}
				}
			}
			else {
				this.tokens.push(Token.error(current));
			}
			//console.log("processing " + current +", tokens: " +this.tokens);
		}
	}
}

//utilities
function arrayWithoutElement(array, e) {
	var x;
	var remainder = [];
	for (x in array) {
		if (array[x] !== e) {
			remainder.push(array[x]);
		}
	}
	return remainder;
}

function arrayContains(array, e) {
	var x;
	for (x in array) {
		if (array[x] === e) {
			return true;
		}
	}
	return false;
};

function arrayContainsArray(array1, array2) {
	var x;
	for (x in array2) {
		var a = arrayContains(array1, array2[x]);
		if (a!==true) { return false };
	}
	return true;
};

function addOrRemove(array, e) {
	if (arrayContains(array,e)){
		return arrayWithoutElement(array, e);
	} else {
		array.push(e);
		return array;
	}
};

function removeElement(array, e) {
	var newArray = [];
	var x;
	for (x in array) {
		if (e !== array[x]) {
			newArray.push(array[x]);
		}
	}
	return newArray;
}



/**
* Randomization Utilities
*/

function randomInt(lessThan){
	return Math.floor(Math.random()*lessThan);
};

/**
* returns a pseudo-random integer in the range 
* [greaterThan, lessThan]
*
*/
function randomRange(greaterThan, lessThan){
	var shifted = randomInt(lessThan - greaterThan + 1);
	return lessThan - shifted; 
};

function randomElement(array) {
	var res =randomRange(0, array.length-1);
	return array[res];
};

function shuffle(array) {
  var currentIndex = array.length, temporaryValue, randomIndex;
  // While there remain elements to shuffle...
  while (0 !== currentIndex) {
    // Pick a remaining element...
    randomIndex = randomRange(0, currentIndex -1);
    currentIndex -= 1;
    // And swap it with the current element.
    temporaryValue = array[currentIndex];
    array[currentIndex] = array[randomIndex];
    array[randomIndex] = temporaryValue;
  }
  return array;
};

function prettyPrintList(list) {
	var s = "";
	var i;
	for (i in list) {
		if (i != 0 && list.length != 2) {
			s +=",";
		} 
		if (i == list.length -1 && list.length !== 1) {
			s += " and";
		}
		s += " ";
		s += list[i];	
	}
	return s;
}

function completeSentence(sentence) {
	return sentence.charAt(0).toUpperCase() + sentence.substr(1) + ".";
}
