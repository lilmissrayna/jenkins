let login = document.getElementById("login-btn");
let register = document.getElementById("register-btn");
let loginForm = document.getElementById("login-form");
let registerForm = document.getElementById("register-form");
let loginSubmit = document.getElementById("login-submit");
let registerSubmit = document.getElementById("register-submit");
const BASE_URL = "http://localhost:9000/";

login.addEventListener('click', loginMenu);
register.addEventListener('click', registerMenu);
//loginSubmit.addEventListener('click', authenticate);
registerSubmit.addEventListener('click', registerAccount);

function loginMenu() {
    registerForm.style.display = "none";
    register.classList.remove("btn-primary");
    register.classList.add("btn-secondary");
    login.classList.remove("btn-secondary");
    login.classList.add("btn-primary");
    loginForm.style.display = "block";
}

function registerMenu() {
    loginForm.style.display = "none";
    login.classList.remove("btn-primary");
    login.classList.add("btn-secondary");
    register.classList.remove("btn-secondary");
    register.classList.add("btn-primary");
    registerForm.style.display = "block";
}
 
function registerAccount() {
	let username = document.getElementById('register-username').value;
	let email = document.getElementById('email').value;
	
	fetch(BASE_URL + 'user', {
    	method: 'POST',
    	headers: {'Content-Type': 'application/x-www-form-url-encoded', 'Accept': 'application/json'},
    	body: 'username=' + username + '&email='+ email
	})
}

//function authenticate() {
	
//	let username = document.getElementById('username').value;
//	let password = document.getElementById('password').value;
	
	//fetch(BASE_URL + 'authenticate', {
   // 	method: 'POST',
  //  	headers: {'Content-Type': 'application/x-www-form-url-encoded', 'Accept': 'application/json'},
 //   	body: 'username=' + username + '&password='+ password
//	})
//	.then(response=>console.log(response))
//}