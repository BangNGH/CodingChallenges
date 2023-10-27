
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
    const postURL = 'https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=false&fields=*';
    const options = {
        method: 'POST',
        headers: {
            'X-RapidAPI-Key': MY_API_KEY,
            'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
        },
        body: new URLSearchParams({
            language_id: option,
            source_code: editor.getValue(),
            stdin: input.value
        })
    };

    try {
        const response = await fetch(postURL, options);
        const postResult = await response.json();
        console.log(postResult);
        console.log(editor.getValue());

        const checkStatus = async () => {
            const getURL = 'https://judge0-ce.p.rapidapi.com/submissions/'+postResult.token+'/?base64_encoded=false&fields=*';
            const getOptions = {
                method: 'GET',
                headers: {
                    'X-RapidAPI-Key': MY_API_KEY,
                    'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
                }
            };

            const getResult = await fetch(getURL, getOptions);
            const result = await getResult.json();
            console.log(result);
            console.log( input.value);

            if (result.status && result.status.id <= 2) {
                // Nếu trạng thái là "In Queue" hoặc "Processing", thì tiếp tục kiểm tra
                setTimeout(checkStatus, 1000); // Kiểm tra lại sau 1 giây
            } else if (result.status && result.status.id === 3) {
                // Nếu trạng thái là "Accepted", hiển thị output
                if(result.stdout === null){
                    output.value = result.compile_output;
                } else {
                    output.value = result.stdout;
                }
                loading.style.display = "none";
            } else {
                // Nếu trạng thái không rõ ràng, hiển thị thông báo lỗi
                output.value = "Error occurred while processing the code.";
                output.value = result.compile_output;
                loading.style.display = "none";
            }
        };

        // Bắt đầu kiểm tra trạng thái
        checkStatus();
    } catch (error) {
        console.error(error);
        loading.style.display = "none";
    }

/*    const settings = {
        async: true,
        crossDomain: true,
        url: 'https://judge0-ce.p.rapidapi.com/submissions/'+postResult.token+'/?base64_encoded=false&fields=*',
        method: 'GET',
        headers: {
            'X-RapidAPI-Key': MY_API_KEY,
            'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
        }
    };

    $.ajax(settings).done(function (response) {
        console.log(response);
     /!*   if(result.stdout === null){
            output.value = result.compile_output;
        }else{
            output.value = result.stdout
        }*!/
        loading.style.display = "none";
    });*/

  /*  const getURL = 'https://judge0-ce.p.rapidapi.com/submissions/'+postResult.token+'/?base64_encoded=false&fields=*';
    const getOptions = {
        method: 'GET',
        headers: {
            'X-RapidAPI-Key': MY_API_KEY,
            'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
        }
    };

    try {
        const response = await fetch(getURL, getOptions);
        const result = await response.json();
        console.log(result);
        if(result.stdout === null){
            output.value = result.compile_output;
        }else{
            output.value = result.stdout
        }
        loading.style.display = "none";
    } catch (error) {
        console.error(error);
    }*/
})