
var input = document.getElementById("input")
var output = document.getElementById("output")
var run = document.getElementById("run")
var loading = document.getElementById("water-loading")
input.style.backgroundColor = "#282a36";
input.style.color = "#fff"
output.style.backgroundColor = "#282a36";
output.style.color = "#fff"
var theme = document.getElementById("theme")
theme.addEventListener("change", function () {
    if (theme.value === "Dark") {
        editor.setOption("theme", "dracula")
        input.style.backgroundColor = "#282a36";
        input.style.color = "#fff"
        output.style.backgroundColor = "#282a36";
        output.style.color = "#fff"
    }
    if (theme.value === "Light") {
        editor.setOption("theme", "3024-day")
        input.style.backgroundColor = "#f7f7f7";
        input.style.color = "#000";
        output.style.backgroundColor = "#f7f7f7";
        output.style.color = "#000";
    }
    if (theme.value === "Night") {
        editor.setOption("theme", "night")
        input.style.backgroundColor = "#0a001f";
        input.style.color = "#f8f8f8";
        output.style.backgroundColor = "#0a001f";
        output.style.color = "#f8f8f8";
    }
    if (theme.value === "Idea") {
        editor.setOption("theme", "idea")
        input.style.backgroundColor = "#fff";
        input.style.color = "#000";
        output.style.backgroundColor = "#fff";
        output.style.color = "#000";
    }
    if (theme.value === "Ocenaic") {
        editor.setOption("theme", "oceanic-next")
        input.style.backgroundColor = "#304148";
        input.style.color = "#f8f8f2";
        output.style.backgroundColor = "#304148";
        output.style.color = "#f8f8f2";
    }
})
import {MY_API_KEY} from "./config.js"
run.addEventListener("click", async function () {
    output.value = "running...";
    loading.style.display = "block";
    const url = 'https://codex7.p.rapidapi.com/';
    const options = {
        method: 'POST',
        headers: {
            'content-type': 'application/x-www-form-urlencoded',
            'X-RapidAPI-Key': MY_API_KEY,
            'X-RapidAPI-Host': 'codex7.p.rapidapi.com'
        },
        body: new URLSearchParams({
            code: editor.getValue() ,
            language: option,
            input : input.value
        })
    };

    try {
        const response = await fetch(url, options);
        const result = await response.json();
        if(result.error !== ""){
            output.value = result.error;
        }else{
            output.value = result.output
        }
        loading.style.display = "none";
    } catch (error) {
        console.error(error);
    }
})