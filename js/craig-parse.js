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
	latex(){
		let l = "";
		if (!this.sign){
			l += "\\neg ";
		}
		l += this.symbol;
		return l;
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

	latex() {
		let d = "";
		let first = true;
		let containsProps = true;
		for (var i in this.propositions){
			containsProps = this.propositions[i].isProposition();				
			if (!first) {
				d += "\\vee ";
			}
			first = false;
			if (containsProps) {
				d += this.propositions[i].latex();
			} else {
				d += "(" + this.propositions[i].latex() + ")";
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

	latex() {
		let d = "";
		let first = true;
		let containsProps = true;
		for (var i in this.propositions){
			containsProps = this.propositions[i].isProposition();							
			if (!first) {
				d += "\\wedge ";
			}
			first = false;
			if (containsProps) {
				d += this.propositions[i].latex();
			} else {
				d += "(" + this.propositions[i].latex() + ")";
			}
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

	latex() {
		return this.source.latex() + " \\to " + this.target.latex();		
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
		this.propSize = propositions[0].length;	
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
			let propSkip = 0;
			if (ignoreCount > 0){
				ignoreCount --;
				continue;
			}
			let current = this.input[i];
			let larger = current;
			if (this.propSize > 1){
				for (var j = 1; j< this.propSize; j++) {
					propSkip ++;
					let peek0 = this.input[i +j]
					larger += peek0;
				}
			}		
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
			} else if (this.propositions.includes(larger)) {
				this.tokens.push(new Token(larger, 'proposition'));
				ignoreCount += propSkip;
			} else {
				this.tokens.push(Token.error(current));
			}
			//console.log("processing " + current +", tokens: " +this.tokens);
		}
	}
}
