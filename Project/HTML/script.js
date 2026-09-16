function esc(value) {
  return String(value == null ? '' : value)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

const DB={
users:[
{id:'2520030105',password:'student-demo-password',role:'student',name:'Karkala Shiva Reddy'},
{id:'2007',password:'mentor-demo-password',role:'mentor',name:'Dr Srinivas'}
],
requests:[]
};


if(!localStorage.getItem("users")){
localStorage.setItem("users",JSON.stringify(DB.users));
}

if(!localStorage.getItem("requests")){
localStorage.setItem("requests",JSON.stringify(DB.requests));
}

let currentUser=null;


/* LOGIN */

function login(){

let id=document.getElementById("loginId").value;
let pass=document.getElementById("password").value;

let users=JSON.parse(localStorage.getItem("users"));

let user=users.find(u=>u.id===id && u.password===pass);

if(user){

currentUser=user;

document.getElementById("login").style.display="none";

if(user.role==="student"){
document.getElementById("studentDash").style.display="block";
loadStudent();
}
else{
document.getElementById("mentorDash").style.display="block";
loadMentor();
}

}
else{
document.getElementById("error").style.display="block";
}

}


/* LOGOUT */

function logout(){
location.reload();
}


/* STUDENT VIEW */

function loadStudent(){

let req=JSON.parse(localStorage.getItem("requests"));

let my=req.filter(r=>r.studentId===currentUser.id);

let table=document.getElementById("studentTable");

table.innerHTML="";

if(my.length===0){
table.innerHTML="<tr><td colspan='4'>No requests</td></tr>";
return;
}

my.forEach(r=>{

table.innerHTML+=`

<tr>
<td>${esc(r.date)}</td>
<td>${esc(r.time)}</td>
<td>${esc(r.reason)}</td>
<td><span class="badge ${esc(r.status.toLowerCase())}">${esc(r.status)}</span></td>
</tr>

`;

});

}


/* APPLY PASS */

function applyPass(){

let date=document.getElementById("date").value;
let time=document.getElementById("time").value;
let reason=document.getElementById("reason").value;

if(date==="" || time==="" || reason===""){
alert("Please fill all fields");
return;
}

let req=JSON.parse(localStorage.getItem("requests"));

let newReq={
id:Date.now(),
studentId:currentUser.id,
date:date,
time:time,
reason:reason,
status:"Pending"
};

req.push(newReq);

localStorage.setItem("requests",JSON.stringify(req));

alert("Gate Pass Request Submitted");

loadStudent();

}


/* MENTOR VIEW */

function loadMentor(){

let req=JSON.parse(localStorage.getItem("requests"));
let users=JSON.parse(localStorage.getItem("users"));

let table=document.getElementById("mentorTable");

table.innerHTML="";

if(req.length===0){
table.innerHTML="<tr><td colspan='5'>No requests</td></tr>";
return;
}

req.forEach(r=>{

let student=users.find(u=>u.id===r.studentId);
let name=student?student.name:r.studentId;

table.innerHTML+=`

<tr>

<td>${esc(name)}</td>
<td>${esc(r.date)}</td>
<td>${esc(r.time)}</td>
<td>${esc(r.reason)}</td>

<td>

${r.status==="Pending" ? `

<button class="action" onclick="update(${esc(r.id)},'Approved')" style="background:green;color:white;">Approve</button>

<button class="action" onclick="update(${esc(r.id)},'Rejected')" style="background:red;color:white;">Reject</button>

` :

`<span class="badge ${esc(r.status.toLowerCase())}">${esc(r.status)}</span>`}

</td>

</tr>

`;

});

}


/* UPDATE STATUS */

function update(id,status){

let req=JSON.parse(localStorage.getItem("requests"));

let index=req.findIndex(r=>r.id===id);

req[index].status=status;

localStorage.setItem("requests",JSON.stringify(req));

loadMentor();

alert("Request "+status);

}
