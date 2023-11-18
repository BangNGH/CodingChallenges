const testCasesContainer = document.getElementById('test-cases-container');
const resultSection = document.getElementById('result');
var run = document.getElementById("run")
var submit = document.getElementById("submit")
var theme = document.getElementById("theme")
const excuting = document.getElementById('excuting');
const assignment_id = document.getElementById('hidden_input');
const assignment_kit_idElement = document.getElementById('assignment_kit_id');
const number_of_assignments = document.getElementById('number_of_assignments');
const user_id = document.getElementById('user_id');
let check = 0;
const submissionValues = [];
document.addEventListener("DOMContentLoaded", (event) => {
    const run = document.getElementById('run');
    const submit = document.getElementById('submit');
    run.setAttribute("disabled", "");
    submit.setAttribute("disabled", "");
    document.getElementById('courseTab').addEventListener('shown.bs.tab', function (event) {
        const activeTab = event.target; // Tab đang active
        const previousTab = event.relatedTarget; // Tab trước đó

        // Kiểm tra tab và enable/disable các nút tương ứng
        if (activeTab.id === 'description-tab') {
            run.setAttribute("disabled", "");
            submit.setAttribute("disabled", "");
        } else {
            run.removeAttribute("disabled");
            submit.removeAttribute("disabled");
        }
    });
    for (let i = 1; i <= number_of_assignments.value; i++) {
        var input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'submission_id';
        input.id = 'submission_id_' + (i);
        document.body.appendChild(input);
    }
});

function encode(str) {
    return btoa(unescape(encodeURIComponent(str || "")));
}

function decode(bytes) {
    var escaped = escape(atob(bytes || ""));
    return decodeURIComponent(escaped);

}

theme.addEventListener("change", function () {
    if (theme.value === "Dark") {
        editor.setOption("theme", "dracula")

    }
    if (theme.value === "Light") {
        editor.setOption("theme", "3024-day")
    }
    if (theme.value === "Night") {
        editor.setOption("theme", "night")
    }
    if (theme.value === "Idea") {
        editor.setOption("theme", "idea")
    }
    if (theme.value === "Ocenaic") {
        editor.setOption("theme", "oceanic-next")
    }
})
import {MY_API_KEY} from "./config.js"


//batch submission
run.addEventListener("click", async function () {
    testCasesContainer.innerHTML = '';
    excuting.style.display = 'block';
    const bottomElement = document.getElementById('excuting');
    bottomElement.scrollIntoView({behavior: 'smooth'});
    let count = 0;
    let submissions;
    $.ajax({
        url: '/api/submissions/get-assignment-info',
        type: 'GET',
        data: {assignment_id: assignment_id.value},
        success: async (data) => {
            const dataJson = JSON.parse(data);
            const testCases = dataJson.sampleTestCases;
            const time_limit = dataJson.time_limit;
            const memory_limit = dataJson.memory_limit;
            if (time_limit != 0 && memory_limit != 0) {
                submissions = testCases.map(testCase => {
                    // Tạo đối tượng submission cho mỗi test case
                    return {
                        language_id: option,
                        source_code: editor.getValue(),
                        stdin: testCase.input,
                        expected_output: testCase.expectedOutput,
                        cpu_time_limit: time_limit,
                        memory_limit: memory_limit
                    };
                });
            } else if (time_limit != 0 && memory_limit == 0) {
                submissions = testCases.map(testCase => {
                    // Tạo đối tượng submission cho mỗi test case
                    return {
                        language_id: option,
                        source_code: editor.getValue(),
                        stdin: testCase.input,
                        expected_output: testCase.expectedOutput,
                        cpu_time_limit: time_limit
                    };
                });
            } else if (time_limit == 0 && memory_limit != 0) {
                submissions = testCases.map(testCase => {
                    // Tạo đối tượng submission cho mỗi test case
                    return {
                        language_id: option,
                        source_code: editor.getValue(),
                        stdin: testCase.input,
                        expected_output: testCase.expectedOutput,
                        memory_limit: memory_limit
                    };
                });
            } else {
                submissions = testCases.map(testCase => {
                    // Tạo đối tượng submission cho mỗi test case
                    return {
                        language_id: option,
                        source_code: editor.getValue(),
                        stdin: testCase.input,
                        expected_output: testCase.expectedOutput
                    };
                });
            }
            console.log(submissions);

            const postURL = 'https://judge0-ce.p.rapidapi.com/submissions/batch?base64_encoded=false';
            const options = {
                method: 'POST',
                headers: {
                    'content-type': 'application/json',
                    'Content-Type': 'application/json',
                    'X-RapidAPI-Key': MY_API_KEY,
                    'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
                },
                body: JSON.stringify({
                    submissions: submissions
                })
            };

            try {
                const response = await fetch(postURL, options);
                const postResult = await response.json();
                const tokens = postResult.map(item => item.token);
                const tokensString = tokens.join(',');
                console.log("Tokens: " + tokensString);
                const checkStatus = async () => {
                    const getURL = `https://ce.judge0.com/submissions/batch?tokens=${tokensString}&base64_encoded=true&fields=source_code,stdout,stdin,memory,time,token,compile_output,message,status,expected_output`;
                    const getOptions = {
                        method: 'GET',
                        headers: {
                            'X-RapidAPI-Key': MY_API_KEY,
                            'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
                        }
                    };
                    try {
                        const getResult = await fetch(getURL, getOptions);
                        const result = await getResult.json();
                        console.log(result);
                        //"Processing" - "In Queue"
                        const processingSubmissions = result.submissions.filter(submission => submission.status && submission.status.id <= 2);
                        if (processingSubmissions.length >= 1) {
                            setTimeout(() => {
                                checkStatus();
                            }, 4000);
                        } else {
                            result.submissions.every((submission, index) => {
                                const newContainer = document.createElement('div');
                                newContainer.classList.add('col-xxl-10', 'offset-xxl-1', 'col-xl-10', 'offset-xl-1', 'col-lg-10', 'offset-lg-1');
                                if (submission.status && submission.status.id === 3) {
                                    //Accepted
                                    var stdin = decode(submission.stdin);
                                    var stdout = decode(submission.stdout);
                                    var expected_output = decode(submission.expected_output);

                                    count = count + 1;
                                    let isSampleTestCase;
                                    if (count <= testCases.length) {
                                        isSampleTestCase = true;
                                    } else isSampleTestCase = false;

                                    newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                   <span>${submission.memory}KB</span>
                                                   <span>Sucess</span>
                </div>
                <h3 class="events__title">
                    <a class="price__features">
                        <i class="far fa-check" style="background: rgb(48 168 32 / 9%);;
                            border-radius: 50%;
                            font-size: 25px;"></i> Sample Test case 0${count}
                    </a>
                </h3>
            </div>
             <div class="events__more" style="display: ${isSampleTestCase ? 'block' : 'none'}">
                    <a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
                        Xem chi tiết
                        <i class="far fa-arrow-right"></i>
                        <i class="far fa-arrow-right"></i>
                    </a>
                </div>
                <div class="events__more" style="display: ${isSampleTestCase ? 'none' : 'block'}">
                    <a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
                        Hidden TestCase
                        <i style="color: #69b35c;" class="far fa-unlock"></i>
                        <i style="color: #e65972;" class="far fa-lock"></i>
                    </a>
                </div>
            </div>
        </div>
        <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
            <div class="contact__info white-bg p-relative z-index-1">
                <div class="contact__info-inner white-bg">
                    <ul>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Compiler Message</h4>
                                    <pre class="pre-tags" style="font-weight: initial; text-align: center" id="message">Thành công</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Input</h4>
                                    <pre class="pre-tags" id="input">${stdin}</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Output</h4>
                                    <pre class="pre-tags" id="my_output">${stdout}</pre>
                                </div>
                            </div>
                        </li>
                     
                    </ul>
                </div>
            </div>
        </div>
 
    `;
                                    // Thêm container vào container chứa test cases
                                    testCasesContainer.appendChild(newContainer);
                                    const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                    const testcaseDetails = document.getElementById(`event-details-${count}`);
                                    viewMoreButton.addEventListener('click', () => {
                                        if (testcaseDetails.style.display === 'block') {
                                            testcaseDetails.style.display = 'none';
                                        } else {
                                            testcaseDetails.style.display = 'block';
                                        }
                                    });
                                    excuting.style.display = 'none';
                                    resultSection.style.display = 'block';
                                } else if (submission.status && submission.status.id === 4) {
                                    //Wrong Answer
                                    var stdin = decode(submission.stdin);
                                    var stdout = decode(submission.stdout);
                                    var expected_output = decode(submission.expected_output);
                                    count = count + 1;
                                    newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                   <span>${submission.memory} KB</span>
                                                   <span>Sai kết quả</span>
                </div>
                <h3 class="events__title" style="color: #d12a47;">
                    <a class="price__features">
                        <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                            border-radius: 50%;
                            font-size: 25px;"></i>  Sample Test case 0${count}
                    </a>
                </h3>
            </div>
            <div class="events__more">
                <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                    Xem chi tiết
                    <i class="far fa-arrow-right"></i>
                    <i class="far fa-arrow-right"></i>
                </a>
            </div>
            </div>
        </div>
        <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
            <div class="contact__info white-bg p-relative z-index-1">
                <div class="contact__info-inner white-bg">
                    <ul>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Compiler Message</h4>
                                    <pre class="pre-tags" style="font-weight: initial" id="message">Kết quả không trùng khớp</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Input</h4>
                                    <pre class="pre-tags" id="input">${stdin}</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Your Output</h4>
                                    <pre class="pre-tags" id="my_output">${stdout}</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Expected Output</h4>
                                    <pre class="pre-tags" id="expected_output">${expected_output}</pre>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
 
    `;
                                    // Thêm container vào container chứa test cases
                                    testCasesContainer.appendChild(newContainer);
                                    const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                    const testcaseDetails = document.getElementById(`event-details-${count}`);
                                    viewMoreButton.addEventListener('click', () => {
                                        if (testcaseDetails.style.display === 'block') {
                                            testcaseDetails.style.display = 'none';
                                        } else {
                                            testcaseDetails.style.display = 'block';
                                        }
                                    });
                                    excuting.style.display = 'none';
                                    resultSection.style.display = 'block';
                                    return false;
                                } else if (submission.status && submission.status.id === 11) {
                                    var message = decode(submission.message);
                                    var description = (submission.status.description);
                                    var output = [message, description].join("\n").trim();

                                    let submission_add_to_list = {
                                        executionTime: submission.time,
                                        memory: submission.memory,
                                        my_output: output,
                                        expected_output: submission.expected_output,
                                        stdin: submission.stdin,
                                        ispassed: false
                                    };
                                    submissions_to_send.push(submission_add_to_list);
                                    count = count + 1;
                                    newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                   <span>${submission.memory} KB</span>
                                                   <span>Error Status 11</span>
                </div>
                <h3 class="events__title">
                    <a class="price__features" style="color: #d12a47;">
                        <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                            border-radius: 50%;
                            font-size: 25px;"></i>Test case 0${count}
                    </a>
                </h3>
            </div>
            <div class="events__more">
                <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                    Xem chi tiết
                    <i class="far fa-arrow-right"></i>
                    <i class="far fa-arrow-right"></i>
                </a>
            </div>
            </div>
        </div>
        <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
            <div class="contact__info white-bg p-relative z-index-1">
                <div class="contact__info-inner white-bg">
                    <ul>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Compiler Message</h4>
                                    <pre class="pre-tags" style="font-weight: initial" id="message">${output}</pre>
                                
                                </div>
                            </div>
                        </li>
                   
                    </ul>
                </div>
            </div>
        </div>
 
    `;
                                    // Thêm container vào container chứa test cases
                                    testCasesContainer.appendChild(newContainer);
                                    const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                    const testcaseDetails = document.getElementById(`event-details-${count}`);
                                    viewMoreButton.addEventListener('click', () => {
                                        if (testcaseDetails.style.display === 'block') {
                                            testcaseDetails.style.display = 'none';
                                        } else {
                                            testcaseDetails.style.display = 'block';
                                        }
                                    });
                                    excuting.style.display = 'none';
                                    resultSection.style.display = 'block';
                                    return false;
                                } else {

                                    var stdout = decode(submission.stdout);
                                    var compile_output = decode(submission.compile_output);
                                    var output = [compile_output, stdout].join("\n").trim();
                                    console.log("status ID: " + submission.status.id);
                                    count = count + 1;
                                    newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                   <span>${submission.memory} KB</span>
                                                  <span>Error Status ${submission.status.id}</span>
                </div>
                <h3 class="events__title">
                    <a class="price__features" style="color: #d12a47;">
                        <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                            border-radius: 50%;
                            font-size: 25px;"></i>  Sample Test case 0${count}
                    </a>
                </h3>
            </div>
            <div class="events__more">
                <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                    Xem chi tiết
                    <i class="far fa-arrow-right"></i>
                    <i class="far fa-arrow-right"></i>
                </a>
            </div>
            </div>
        </div>
        <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
            <div class="contact__info white-bg p-relative z-index-1">
                <div class="contact__info-inner white-bg">
                    <ul>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Compiler Message</h4>
                                    <pre class="pre-tags" style="font-weight: initial" id="message">${output}</pre>
                                 
                                </div>
                            </div>
                        </li>
                   
                    </ul>
                </div>
            </div>
        </div>
 
    `;
                                    // Thêm container vào container chứa test cases
                                    testCasesContainer.appendChild(newContainer);
                                    const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                    const testcaseDetails = document.getElementById(`event-details-${count}`);
                                    viewMoreButton.addEventListener('click', () => {
                                        if (testcaseDetails.style.display === 'block') {
                                            testcaseDetails.style.display = 'none';
                                        } else {
                                            testcaseDetails.style.display = 'block';
                                        }
                                    });
                                    excuting.style.display = 'none';
                                    resultSection.style.display = 'block';
                                    return false;
                                }
                                return true;
                            });
                        }


                    } catch (error) {
                        console.log(error);
                    }
                };
                await checkStatus();
            } catch (error) {
                console.error(error);
            }

        },
        error: (error) => {
            console.log(error);
            console.log("Error with id: " + assignment_id.value);
        }
    });

});

submit.addEventListener("click", async function () {

    if (confirm('Are you sure to submit this challenge?')) {
        testCasesContainer.innerHTML = '';
        let submissions_to_send = [];
        const langagueName = langague_name;
        const source_code = editor.getValue();
        excuting.style.display = 'block';
        const bottomElement = document.getElementById('excuting');
        bottomElement.scrollIntoView({behavior: 'smooth'});
        let count = 0;
        let submissions;
        $.ajax({
            url: '/api/submissions/get-assignment-info',
            type: 'GET',
            data: {assignment_id: assignment_id.value},
            success: async (data) => {
                const dataJson = JSON.parse(data);
                const allTestCases = dataJson.allTestCases;
                const time_limit = dataJson.time_limit;
                const memory_limit = dataJson.memory_limit;
                const testCases = dataJson.sampleTestCases;
                if (time_limit != 0 && memory_limit != 0) {
                    submissions = allTestCases.map(testCase => {
                        // Tạo đối tượng submission cho mỗi test case
                        return {
                            language_id: option,
                            source_code: editor.getValue(),
                            stdin: testCase.input,
                            expected_output: testCase.expectedOutput,
                            cpu_time_limit: time_limit.value,
                            memory_limit: memory_limit.value
                        };
                    });
                } else if (time_limit != 0 && memory_limit == 0) {
                    submissions = allTestCases.map(testCase => {
                        // Tạo đối tượng submission cho mỗi test case
                        return {
                            language_id: option,
                            source_code: editor.getValue(),
                            stdin: testCase.input,
                            expected_output: testCase.expectedOutput,
                            cpu_time_limit: time_limit.value
                        };
                    });
                } else if (time_limit == 0 && memory_limit != 0) {
                    submissions = allTestCases.map(testCase => {
                        // Tạo đối tượng submission cho mỗi test case
                        return {
                            language_id: option,
                            source_code: editor.getValue(),
                            stdin: testCase.input,
                            expected_output: testCase.expectedOutput,
                            memory_limit: memory_limit.value
                        };
                    });
                } else {
                    submissions = allTestCases.map(testCase => {
                        // Tạo đối tượng submission cho mỗi test case
                        return {
                            language_id: option,
                            source_code: editor.getValue(),
                            stdin: testCase.input,
                            expected_output: testCase.expectedOutput
                        };
                    });
                }
                console.log(submissions);
                const postURL = 'https://judge0-ce.p.rapidapi.com/submissions/batch?base64_encoded=false';
                const options = {
                    method: 'POST',
                    headers: {
                        'content-type': 'application/json',
                        'Content-Type': 'application/json',
                        'X-RapidAPI-Key': MY_API_KEY,
                        'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
                    },
                    body: JSON.stringify({
                        submissions: submissions
                    })
                };

                try {
                    const response = await fetch(postURL, options);
                    const postResult = await response.json();
                    const tokens = postResult.map(item => item.token);
                    const tokensString = tokens.join(',');
                    console.log("Tokens: " + tokensString);
                    const checkSubmitStatus = async () => {
                        const getURL = `https://ce.judge0.com/submissions/batch?tokens=${tokensString}&base64_encoded=true&fields=source_code,stdout,stdin,memory,time,token,compile_output,message,status,expected_output`;
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
                        const processingSubmissions = result.submissions.filter(submission => submission.status && submission.status.id <= 2);
                        if (processingSubmissions.length >= 1) {
                            setTimeout(() => {
                                checkSubmitStatus();
                            }, 4000);
                        } else {
                            result.submissions.every((submission, index) => {

                                /*   const newContainer = document.createElement('div');
                                   newContainer.classList.add('col-xxl-10', 'offset-xxl-1', 'col-xl-10', 'offset-xl-1', 'col-lg-10', 'offset-lg-1');*/
                                if (submission.status && submission.status.id === 3) {
                                    //Accepted
                                    var stdin = decode(submission.stdin);
                                    var stdout = decode(submission.stdout);
                                    var expected_output = decode(submission.expected_output);

                                    let submission_add_to_list = {
                                        executionTime: submission.time,
                                        memory: submission.memory,
                                        my_output: submission.stdout,
                                        expected_output: decode(submission.expected_output),
                                        stdin: decode(submission.stdin),
                                        ispassed: true
                                    };
                                    submissions_to_send.push(submission_add_to_list);


                                    count = count + 1;
                                    let isSampleTestCase;
                                    if (count <= testCases.length) {
                                        isSampleTestCase = true;
                                    } else isSampleTestCase = false;
                                    /*
                                                                        newContainer.innerHTML = `
                                            <div class="events__item mb-10 hover__active">
                                                  <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
                                                <div class="events__content">
                                                    <div class="events__meta">
                                                       <span>${submission.time}s</span>
                                                                                       <span>${submission.memory}KB</span>
                                                                                       <span>Sucess</span>
                                                    </div>
                                                    <h3 class="events__title">
                                                        <a class="price__features">
                                                            <i class="far fa-check" style="background: rgb(48 168 32 / 9%);;
                                                                border-radius: 50%;
                                                                font-size: 25px;"></i> Test case 0${count}
                                                        </a>
                                                    </h3>
                                                </div>

                                                    <div class="events__more" style="display: ${isSampleTestCase ? 'block' : 'none'}">
                                                        <a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
                                                            Xem chi tiết
                                                            <i class="far fa-arrow-right"></i>
                                                            <i class="far fa-arrow-right"></i>
                                                        </a>
                                                    </div>
                                                    <div class="events__more" style="display: ${isSampleTestCase ? 'none' : 'block'}">
                                                        <a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
                                                            Hidden TestCase
                                                            <i style="color: #69b35c;" class="far fa-unlock"></i>
                                                            <i style="color: #e65972;" class="far fa-lock"></i>
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
                                                <div class="contact__info white-bg p-relative z-index-1">
                                                    <div class="contact__info-inner white-bg">
                                                        <ul>
                                                            <li>
                                                                <div class="contact__info-item d-flex align-items-start mb-20">
                                                                    <div class="contact__info-text" style="width: 100%">
                                                                        <h4 class="blue-grey">Compiler Message</h4>
                                                                        <pre class="pre-tags" style="font-weight: initial; text-align: center"" id="message">Thành công</pre>
                                                                    </div>
                                                                </div>
                                                            </li>
                                                            <li>
                                                                <div class="contact__info-item d-flex align-items-start mb-20">
                                                                    <div class="contact__info-text" style="width: 100%">
                                                                        <h4 class="blue-grey">Input</h4>
                                                                        <pre class="pre-tags" id="input">${stdin}</pre>
                                                                    </div>
                                                                </div>
                                                            </li>
                                                            <li>
                                                                <div class="contact__info-item d-flex align-items-start mb-20">
                                                                    <div class="contact__info-text" style="width: 100%">
                                                                        <h4 class="blue-grey">Output</h4>
                                                                        <pre class="pre-tags" id="my_output">${stdout}</pre>
                                                                    </div>
                                                                </div>
                                                            </li>

                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>

                                        `;
                                                                        // Thêm container vào container chứa test cases
                                                                        testCasesContainer.appendChild(newContainer);
                                                                        const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                                                        const testcaseDetails = document.getElementById(`event-details-${count}`);
                                                                        viewMoreButton.addEventListener('click', () => {
                                                                            if (testcaseDetails.style.display === 'block') {
                                                                                testcaseDetails.style.display = 'none';
                                                                            } else {
                                                                                testcaseDetails.style.display = 'block';
                                                                            }
                                                                        });

                                                                        resultSection.style.display = 'block';*/
                                    excuting.style.display = 'none';
                                } else if (submission.status && submission.status.id === 4) {
                                    //Wrong Answer
                                    var stdin = decode(submission.stdin);
                                    var stdout = decode(submission.stdout);
                                    var expected_output = decode(submission.expected_output);

                                    let submission_add_to_list = {
                                        executionTime: submission.time,
                                        memory: submission.memory,
                                        my_output: submission.stdout,
                                        expected_output: submission.expected_output,
                                        stdin: submission.stdin,
                                        ispassed: false
                                    };
                                    submissions_to_send.push(submission_add_to_list);
                                    count = count + 1;
                                    /*
                                             newContainer.innerHTML = `
                 <div class="events__item mb-10 hover__active">
                       <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
                     <div class="events__content">
                         <div class="events__meta">
                            <span>${submission.time}s</span>
                                                            <span>${submission.memory} KB</span>
                                                            <span>Sai kết quả</span>
                         </div>
                         <h3 class="events__title" style="color: #d12a47;">
                             <a class="price__features">
                                 <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                                     border-radius: 50%;
                                     font-size: 25px;"></i>  Sample Test case 0${count}
                             </a>
                         </h3>
                     </div>
                     <div class="events__more">
                         <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                             Xem chi tiết
                             <i class="far fa-arrow-right"></i>
                             <i class="far fa-arrow-right"></i>
                         </a>
                     </div>
                     </div>
                 </div>
                 <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
                     <div class="contact__info white-bg p-relative z-index-1">
                         <div class="contact__info-inner white-bg">
                             <ul>
                                 <li>
                                     <div class="contact__info-item d-flex align-items-start mb-20">
                                         <div class="contact__info-text" style="width: 100%">
                                             <h4 class="blue-grey">Compiler Message</h4>
                                             <pre class="pre-tags" style="font-weight: initial" id="message">Kết quả không trùng khớp</pre>
                                         </div>
                                     </div>
                                 </li>
                                 <li>
                                     <div class="contact__info-item d-flex align-items-start mb-20">
                                         <div class="contact__info-text" style="width: 100%">
                                             <h4 class="blue-grey">Input</h4>
                                             <pre class="pre-tags" id="input">${stdin}</pre>
                                         </div>
                                     </div>
                                 </li>
                                 <li>
                                     <div class="contact__info-item d-flex align-items-start mb-20">
                                         <div class="contact__info-text" style="width: 100%">
                                             <h4 class="blue-grey">Your Output</h4>
                                             <pre class="pre-tags" id="my_output">${stdout}</pre>
                                         </div>
                                     </div>
                                 </li>
                                 <li>
                                     <div class="contact__info-item d-flex align-items-start mb-20">
                                         <div class="contact__info-text" style="width: 100%">
                                             <h4 class="blue-grey">Expected Output</h4>
                                             <pre class="pre-tags" id="expected_output">${expected_output}</pre>
                                         </div>
                                     </div>
                                 </li>
                             </ul>
                         </div>
                     </div>
                 </div>

             `;
                                             // Thêm container vào container chứa test cases
                                             testCasesContainer.appendChild(newContainer);
                                             const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                             const testcaseDetails = document.getElementById(`event-details-${count}`);
                                             viewMoreButton.addEventListener('click', () => {
                                                 if (testcaseDetails.style.display === 'block') {
                                                     testcaseDetails.style.display = 'none';
                                                 } else {
                                                     testcaseDetails.style.display = 'block';
                                                 }
                                             });

                                             resultSection.style.display = 'block';*/
                                    excuting.style.display = 'none';
                                    return false;
                                } else if (submission.status && submission.status.id === 11) {

                                    var message = decode(submission.message);
                                    var description = (submission.status.description);
                                    var output = [message, description].join("\n").trim();

                                    let submission_add_to_list = {
                                        executionTime: submission.time,
                                        memory: submission.memory,
                                        my_output: output,
                                        expected_output: submission.expected_output,
                                        stdin: submission.stdin,
                                        ispassed: false
                                    };
                                    submissions_to_send.push(submission_add_to_list);
                                    count = count + 1;
                                    /*  newContainer.innerHTML = `
          <div class="events__item mb-10 hover__active">
                <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
              <div class="events__content">
                  <div class="events__meta">
                     <span>${submission.time}s</span>
                                                     <span>${submission.memory} KB</span>
                                                     <span>Error Status 11</span>
                  </div>
                  <h3 class="events__title">
                      <a class="price__features" style="color: #d12a47;">
                          <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                              border-radius: 50%;
                              font-size: 25px;"></i>Test case 0${count}
                      </a>
                  </h3>
              </div>
              <div class="events__more">
                  <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                      Xem chi tiết
                      <i class="far fa-arrow-right"></i>
                      <i class="far fa-arrow-right"></i>
                  </a>
              </div>
              </div>
          </div>
          <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
              <div class="contact__info white-bg p-relative z-index-1">
                  <div class="contact__info-inner white-bg">
                      <ul>
                          <li>
                              <div class="contact__info-item d-flex align-items-start mb-20">
                                  <div class="contact__info-text" style="width: 100%">
                                      <h4 class="blue-grey">Compiler Message</h4>
                                      <pre class="pre-tags" style="font-weight: initial" id="message">${output}</pre>

                                  </div>
                              </div>
                          </li>

                      </ul>
                  </div>
              </div>
          </div>

      `;
                                      // Thêm container vào container chứa test cases
                                      testCasesContainer.appendChild(newContainer);
                                      const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                      const testcaseDetails = document.getElementById(`event-details-${count}`);
                                      viewMoreButton.addEventListener('click', () => {
                                          if (testcaseDetails.style.display === 'block') {
                                              testcaseDetails.style.display = 'none';
                                          } else {
                                              testcaseDetails.style.display = 'block';
                                          }
                                      });

                                      resultSection.style.display = 'block';*/
                                    excuting.style.display = 'none';
                                    return false;
                                } else {

                                    var stdout = decode(submission.stdout);
                                    var compile_output = decode(submission.compile_output);
                                    var output = [compile_output, stdout].join("\n").trim();

                                    let submission_add_to_list = {
                                        executionTime: submission.time,
                                        memory: submission.memory,
                                        my_output: output,
                                        expected_output: submission.expected_output,
                                        stdin: submission.stdin,
                                        ispassed: false
                                    };
                                    submissions_to_send.push(submission_add_to_list);
                                    count = count + 1;
                                    /*   newContainer.innerHTML = `
           <div class="events__item mb-10 hover__active">
                 <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
               <div class="events__content">
                   <div class="events__meta">
                      <span>${submission.time}s</span>
                                                      <span>${submission.memory} KB</span>
                                                      <span>Error Status ${submission.status.id}</span>
                   </div>
                   <h3 class="events__title">
                       <a class="price__features" style="color: #d12a47;">
                           <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);
                               border-radius: 50%;
                               font-size: 25px;"></i>Test case 0${count}
                       </a>
                   </h3>
               </div>
               <div class="events__more">
                   <a class="link-btn" id="view-more-button-${count}" style="color: #e65972; cursor: pointer">
                       Xem chi tiết
                       <i class="far fa-arrow-right"></i>
                       <i class="far fa-arrow-right"></i>
                   </a>
               </div>
               </div>
           </div>
           <div id="event-details-${count}" style="display: none; padding: 10px 60px 60px 60px; border-radius: 7px; background-color: white">
               <div class="contact__info white-bg p-relative z-index-1">
                   <div class="contact__info-inner white-bg">
                       <ul>
                           <li>
                               <div class="contact__info-item d-flex align-items-start mb-20">
                                   <div class="contact__info-text" style="width: 100%">
                                       <h4 class="blue-grey">Compiler Message</h4>
                                       <pre class="pre-tags" style="font-weight: initial" id="message">${output}</pre>

                                   </div>
                               </div>
                           </li>

                       </ul>
                   </div>
               </div>
           </div>

       `;
                                       // Thêm container vào container chứa test cases
                                       testCasesContainer.appendChild(newContainer);
                                       const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                                       const testcaseDetails = document.getElementById(`event-details-${count}`);
                                       viewMoreButton.addEventListener('click', () => {
                                           if (testcaseDetails.style.display === 'block') {
                                               testcaseDetails.style.display = 'none';
                                           } else {
                                               testcaseDetails.style.display = 'block';
                                           }
                                       });

                                       resultSection.style.display = 'block';*/
                                    excuting.style.display = 'none';
                                    return false;
                                }
                                return true;
                            });
                            try {
                                const response = await fetch('/api/submissions/add', {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json'
                                    },
                                    body: JSON.stringify({
                                        assignment_id: assignment_id.value,
                                        sourceCode: encode(source_code),
                                        language: langagueName,
                                        testCaseListDTOS: submissions_to_send
                                    })
                                });

                                const result = await response.json();
                                console.log("Submission ID return: " + JSON.stringify(result));
                                if (check != number_of_assignments.value) {
                                    const id = 'submission_id_' + (check + 1);
                                    const submission_input_found = document.getElementById(id);
                                    submission_input_found.value = JSON.stringify(result);
                                    check++;
                                }

                                var currentTab = document.querySelector('.nav-item.activebutton');
                                // Tìm tab tiếp theo
                                var nextTab = currentTab.nextElementSibling;
                                // Kiểm tra nếu không có tab tiếp theo, chọn tab đầu tiên
                                if (!nextTab) {
                                    const submissionInputs = document.querySelectorAll('input[name="submission_id"]');
                                    submissionInputs.forEach(input => {
                                        let submission_kit_to_list = input.value;
                                        submissionValues.push(submission_kit_to_list);
                                    });
                                    const response = await fetch('/api/submissions/end-contest', {
                                        method: 'POST',
                                        headers: {
                                            'Content-Type': 'application/json'
                                        },
                                        body: JSON.stringify({
                                            submissions_id: submissionValues,
                                            assignment_kit_id: assignment_kit_idElement.value
                                        })
                                    });

                                    const result = await response.text();
                                    console.log("Submission_Kit Status: " + (result));
                                    if (result== "passed"){
                                        const url = `/skills-verification/details/${assignment_kit_idElement.value}`;
                                        window.location.href = url;
                                    }else if (result=="failed"){
                                        const url = `/skills-verification/details/${assignment_kit_idElement.value}`;
                                        window.location.href = url;
                                    }else{

                                    }

                                }else {
                                    nextTab.classList.add("activebutton");
                                    currentTab.classList.remove("activebutton");
                                    nextTab.querySelector('button').click();
                                }


                            } catch (error) {
                                console.error("error when call /api/submissions/add: " + error);
                            }
                        }
                    };
                    await checkSubmitStatus();
                } catch (error) {
                    console.error(error);
                }
            },
            error: (error) => {
                console.log(error);
                console.log("Error with id: " + assignment_id.value);
            }
        });
    }

});

