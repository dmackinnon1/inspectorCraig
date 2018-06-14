'use strict'
// url parsing - simplified (no encoded paramss)
function getQueryParameter(url, key){
    let regex = new RegExp("[?&]" + key + "(=([^&#]*)|&|#|$)"),
    results = regex.exec(url); //an array with 3 elements, last is param value
    if (results == null){
    	return null;
    } else {
    	if (results.length < 3) {
    		return null;
    	}
    	return results[2];	
    }
}


//utilities
function arrayWithoutElement(array, e) {
	let x;
	let remainder = [];
	for (x in array) {
		if (array[x] !== e) {
			remainder.push(array[x]);
		}
	}
	return remainder;
}

function arrayContains(array, e) {
	let x;
	for (x in array) {
		if (array[x] === e) {
			return true;
		}
	}
	return false;
};

function arrayContainsArray(array1, array2) {
	let x;
	for (x in array2) {
		let a = arrayContains(array1, array2[x]);
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
	let newArray = [];
	let x;
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
	let shifted = randomInt(lessThan - greaterThan + 1);
	return lessThan - shifted; 
};

function randomElement(array) {
	let res =randomRange(0, array.length-1);
	return array[res];
};

function shuffle(array) {
  let currentIndex = array.length, temporaryValue, randomIndex;
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
	let s = "";
	let i;
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
