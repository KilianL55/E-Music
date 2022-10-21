// function verifInput(id) {
//     const regex = "[0-9]{10}";
//
//     let valueInput = document.getElementById(id).value;
//
//     if (!valueInput.match(regex)) {
//         document.getElementsByClassName("warning")[0].style.display = "block";
//     } else {
//         document.getElementsByClassName("warning")[0].style.display = "none";
//     }
// }
//
// let form = document.forms["signup-form"];
// let buttonSubmit = document.querySelector(".button-submit");
//
// buttonSubmit.addEventListener("click", function (event) {
//     //form.submit();
//     validationForm();
// });
//
// function validationForm() {
//     console.log("Validation du formulaire");
//     let signupForm = document.querySelector(".signup-form");
//     let confirmForm = document.querySelector(".confirm-form");
//
//     signupForm.style.animation = "1s ease-out 0s 1 slideInToRight";
//
//     setTimeout(() => {
//         signupForm.style.display = "none";
//         confirmForm.style.display = "flex";
//         confirmForm.style.animation = "1s ease-out 0s 1 slideInToLeft";
//         }, 1000
//     );
// }