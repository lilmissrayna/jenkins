let financeManagerDiv = document.getElementById("finance-manager-options");
let requestSubmit = document.getElementById('request-submit');
let statusSubmit = document.getElementById('status-submit');
let welcomeUser = document.getElementById('welcome-user');
let pendingToggle = document.getElementById('all-pending');
let approvedToggle = document.getElementById('all-approved');
let rejectedToggle = document.getElementById('all-rejected');
let allRequests = [];
const BASE_URL = "http://localhost:9000/";
const getCookieValue = (name) => (
  document.cookie.match('(^|;)\\s*' + name + '\\s*=\\s*([^;]+)')?.pop() || ''
)

const username = getCookieValue("username");
let isFinanceManagerString = getCookieValue("isFinanceManager");

let isFinanceManager = false;
if (isFinanceManagerString === "true") {
	isFinanceManager = true;
}

let formatter = new Intl.NumberFormat('en-US', {
  style: 'currency',
  currency: 'USD'
});

window.addEventListener('load',startup);

async function startup(){
    if(isFinanceManager){    // if user is a finance manager
        financeManagerDiv.style.display = "block";
        allRequests = await getAllRequests();
        populateAllTable(allRequests);
    }else{
        financeManagerDiv.style.display = "none";
    }
    
    welcomeUser.innerText = "Welcome, " + username + "!";
}

window.onload = function(){

	
	getPastRequests();
	getPendingRequests();
}

requestSubmit.addEventListener('click', createNewRequest);
statusSubmit.addEventListener('click', approveOrDenyRequest);
pendingToggle.addEventListener('change', function(){filterAllTable(allRequests);});
approvedToggle.addEventListener('change', function(){filterAllTable(allRequests);});
rejectedToggle.addEventListener('change', function(){filterAllTable(allRequests);});

function createNewRequest() {
	if(!amount > 0 && amount != null) {
		let amount = document.getElementById('request-amount').value;
		
		fetch(BASE_URL + 'request', {
    		method: 'POST',
    		headers: {'Content-Type': 'application/x-www-form-url-encoded', 'Accept': 'application/json'},
    		body: 'amount=' + amount + '&description='+ description + '&type=' + type
		})
	}
	
	let description = document.getElementById('description').value;
	let type = "";
	if(document.getElementById('travel').checked) {
		type = "TRAVEL";
	}
	else if(document.getElementById('lodging').checked) {
		type = "LODGING";
	}
	else if(document.getElementById('food').checked) {
		type = "FOOD";
	}
	else {
		type = "OTHER";
	}
	
	
}

function getPastRequests() {
	
	fetch(BASE_URL + "past")
  		.then(response => response.json())
  		.then(data => populatePastTable(data));
	
}

function addPastRow(request) {
	
	let pastTableBody = document.getElementById("past-table-body");
	
	let tableRow = document.createElement("tr");
	
	let idColumn = document.createElement("td");
	let typeColumn = document.createElement("td");
	let amountColumn = document.createElement("td");
	let descColumn = document.createElement("td");
	let statusColumn = document.createElement("td");
	
	idColumn.innerText = request.id;
	typeColumn.innerText = request.type;
	amountColumn.innerText = formatter.format(request.amount);
	descColumn.innerText = request.description;
	statusColumn.innerText = request.status;
	
	tableRow.appendChild(idColumn);
	tableRow.appendChild(typeColumn);
	tableRow.appendChild(amountColumn);
	tableRow.appendChild(descColumn);
	tableRow.appendChild(statusColumn);
	
	pastTableBody.appendChild(tableRow);
}

function populatePastTable(requestArray) {
	
	for(let request of requestArray){
		addPastRow(request);
	}
}

function getPendingRequests() {
	
	fetch(BASE_URL + "pending")
  		.then(response => response.json())
  		.then(data => populatePendingTable(data));
	
}

function addPendingRow(request) {
	
	let pendingTableBody = document.getElementById("pending-table-body");
	
	let tableRow = document.createElement("tr");
	
	let idColumn = document.createElement("td");
	let typeColumn = document.createElement("td");
	let amountColumn = document.createElement("td");
	let descColumn = document.createElement("td");
	let statusColumn = document.createElement("td");
	
	idColumn.innerText = request.id;
	typeColumn.innerText = request.type;
	amountColumn.innerText = formatter.format(request.amount);
	descColumn.innerText = request.description;
	statusColumn.innerText = request.status;
	
	tableRow.appendChild(idColumn);
	tableRow.appendChild(typeColumn);
	tableRow.appendChild(amountColumn);
	tableRow.appendChild(descColumn);
	tableRow.appendChild(statusColumn);
	
	pendingTableBody.appendChild(tableRow);
}

function populatePendingTable(requestArray) {
	
	for(let request of requestArray){
		addPendingRow(request);
	}
}

async function getAllRequests() {
	
	const response = await fetch(BASE_URL + "all");
	
	return response.json();
}

function addAllRow(request) {
	
	let allTableBody = document.getElementById("all-table-body");
	
	let tableRow = document.createElement("tr");
	
	let idColumn = document.createElement("td");
	let typeColumn = document.createElement("td");
	let amountColumn = document.createElement("td");
	let descColumn = document.createElement("td");
	let statusColumn = document.createElement("td");
	
	idColumn.innerText = request.id;
	typeColumn.innerText = request.type;
	amountColumn.innerText = formatter.format(request.amount);
	descColumn.innerText = request.description;
	statusColumn.innerText = request.status;
	
	tableRow.appendChild(idColumn);
	tableRow.appendChild(typeColumn);
	tableRow.appendChild(amountColumn);
	tableRow.appendChild(descColumn);
	tableRow.appendChild(statusColumn);
	
	allTableBody.appendChild(tableRow);
}

function populateAllTable(requestArray) {
	
	for(let request of requestArray){
		addAllRow(request);
	}
}

function resetTable() {
		
	let allTableBody = document.getElementById("all-table-body");
	allTableBody.innerHTML = "";
}

function filterAllTable(requestArray) {
	let newArray = [];
	resetTable();
	if(pendingToggle.checked && !approvedToggle.checked && !rejectedToggle.checked) {
		for(let request of requestArray) {
			if(request.status == "PENDING") {
				newArray.push(request);
			}
		}
		
		populateAllTable(newArray);
	}
	else if(!pendingToggle.checked && approvedToggle.checked && !rejectedToggle.checked) {
		for(let request of requestArray) {
			if(request.status == "APPROVED") {
				newArray.push(request);
			}
		}
		
		populateAllTable(newArray);
	}
	else if(!pendingToggle.checked && !approvedToggle.checked && rejectedToggle.checked) {
		for(let request of requestArray) {
			if(request.status == "REJECTED") {
				newArray.push(request);
			}
		}
		
		populateAllTable(newArray);
	}
	else if(pendingToggle.checked && approvedToggle.checked && !rejectedToggle.checked) {
		for(let request of requestArray) {
			if(request.status == "APPROVED" || request.status == "PENDING") {
				newArray.push(request);
			}
		}
		
		populateAllTable(newArray);
	}
	else if(!pendingToggle.checked && approvedToggle.checked && rejectedToggle.checked) {
		for(let request of requestArray) {
			if(request.status == "APPROVED" || request.status == "REJECTED") {
				newArray.push(request);
			}
		}
		
		populateAllTable(newArray);
	}
	else if(pendingToggle.checked && !approvedToggle.checked && rejectedToggle.checked) {
		
		for(let request of requestArray) {
			if(request.status == "REJECTED" || request.status == "PENDING") {
				newArray.push(request);
			}
		}
		
		populateAllTable(newArray);
	}
	else if(pendingToggle.checked && approvedToggle.checked && rejectedToggle.checked) {
				
		populateAllTable(requestArray);
	}
	else {
		populateAllTable(newArray);
	}
}

function approveOrDenyRequest() {
	let id = document.getElementById('request-id').value;
	let status = "";
	
	if (document.getElementById('approved').checked) {
		status = "APPROVED";
	}
	else {
		status = "REJECTED";
	}
	
	fetch(BASE_URL + 'request', {
    	method: 'PUT',
    	headers: {'Content-Type': 'application/x-www-form-url-encoded', 'Accept': 'application/json'},
    	body: 'id=' + id + '&status='+ status
	})
}