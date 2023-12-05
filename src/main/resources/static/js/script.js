/**
 *
 */
function verify(){
    var pass1 = document.forms['form']['password'].value;
    var pass2 = document.forms['form']['confirmPassword'].value;
    if(pass1 == null || pass1 =="" || pass1 != pass2)
        document.getElementById("error").innerHTML="Please check your passwords";
    return false;
}