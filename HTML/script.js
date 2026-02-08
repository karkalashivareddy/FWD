console.log("js loaded");

const signinForm = document.querySelector(".form.signin");
const signupForm = document.querySelector(".form.signup");

function toggleView() {
  signinForm.classList.toggle("active");
  signupForm.classList.toggle("active");
}