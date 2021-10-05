/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
console.log("Hello User");
//alert("Hello world");

const toggleSlidder = () => {
    if ($(".nav_section").is(":visible")) {

        $(".nav_section").css("display", "none");
        $(".main_section").css("margin", "auto");
    } else {

        $(".nav_section").css("display", "block");

    }
}

