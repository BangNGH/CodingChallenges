const testCasesContainer = document.getElementById('test-cases-container');
const resultSection = document.getElementById('result');
var run = document.getElementById("run")
var submit = document.getElementById("submit")
var theme = document.getElementById("theme")
const excuting = document.getElementById('excuting');

const testCasesElement = document.getElementById('testCases');
const testCasesJSON = testCasesElement.getAttribute('data-test-cases');

const assignment_idElement = document.getElementById('assignment_id');
const assignment_id = assignment_idElement.getAttribute('data-assignment_id');

const allTestCase = document.getElementById('allTestCase');
const allTestCaseJSON = allTestCase.getAttribute('data-test-cases');
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
        output.style.backgroundColor = "#282a36";
        output.style.color = "#fff"

    }
    if (theme.value === "Light") {
        editor.setOption("theme", "3024-day")
    }
    if (theme.value === "Night") {
        editor.setOption("theme", "night")
        output.style.backgroundColor = "#0a001f";
        output.style.color = "#f8f8f8";
    }
    if (theme.value === "Idea") {
        editor.setOption("theme", "idea")
        output.style.backgroundColor = "#fff";
        output.style.color = "#000";
    }
    if (theme.value === "Ocenaic") {
        editor.setOption("theme", "oceanic-next")
        output.style.backgroundColor = "#304148";
        output.style.color = "#f8f8f2";
    }
})
import {MY_API_KEY} from "./config.js"


//batch submission
run.addEventListener("click", async function () {
    testCasesContainer.innerHTML = '';
    const testCases = JSON.parse(testCasesJSON); // Giả sử testCasesJSON chứa danh sách các test case dưới dạng JSON.
    excuting.style.display = 'block';
    const bottomElement = document.getElementById('excuting');
    bottomElement.scrollIntoView({ behavior: 'smooth' });
    let count =0;
    const submissions = testCases.map(testCase => {
        // Tạo đối tượng submission cho mỗi test case
        return {
            language_id: option,
            source_code: editor.getValue(),
            stdin: testCase.input,
            expected_output: testCase.expectedOutput
        };
    });
    console.log(submissions);
    console.log(editor.getValue());

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
                    const processingSubmissions = result.submissions.filter(submission => submission.status && submission.status.id <=2);
                    if (processingSubmissions.length >=1) {
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
                                count=count+1;
                                console.log("COUNT: " + count);
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
            <div class="events__more">
                <a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
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
                                    <pre class="pre-tags" style="font-weight: initial" id="message">Thành công</pre>
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
                            }else if (submission.status && submission.status.id === 4) {
                                //Wrong Answer
                                var stdin = decode(submission.stdin);
                                var stdout = decode(submission.stdout);
                                var expected_output = decode(submission.expected_output);
                                count=count+1;
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
                            }
                            else {

                                var stdout = decode(submission.stdout);
                                var compile_output = decode(submission.compile_output);
                                var output = [compile_output, stdout].join("\n").trim();
                                console.log("status ID: "+submission.status.id);
                                count=count+1;
                                newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                   <span>${submission.memory} KB</span>
                                                   <span>Lỗi biên dịch</span>
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
});

submit.addEventListener("click", async function () {
    testCasesContainer.innerHTML = '';
    let submissions_to_send = [];
    const allTestCases = JSON.parse(allTestCaseJSON); // Giả sử allTestCases chứa danh sách các test case dưới dạng JSON.
    const langagueName = langague_name;
    const source_code = editor.getValue();
    excuting.style.display = 'block';
    const bottomElement = document.getElementById('excuting');
    bottomElement.scrollIntoView({ behavior: 'smooth' });
    let count =0;
    const submissions = allTestCases.map(testCase => {
        // Tạo đối tượng submission cho mỗi test case
        return {
            language_id: option,
            source_code: editor.getValue(),
            stdin: testCase.input,
            expected_output: testCase.expectedOutput
        };
    });
    console.log(submissions);
    console.log(editor.getValue());
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
                const processingSubmissions = result.submissions.filter(submission => submission.status && submission.status.id <=2);
                if (processingSubmissions.length >=1) {
                    setTimeout(() => {
                        checkSubmitStatus();
                    }, 4000);
                }else {
                    result.submissions.every((submission, index) => {

                        const newContainer = document.createElement('div');
                        newContainer.classList.add('col-xxl-10', 'offset-xxl-1', 'col-xl-10', 'offset-xl-1', 'col-lg-10', 'offset-lg-1');
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


                            count=count+1;
                            console.log("COUNT: " + count);
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
            <div class="events__more">
                <a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
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
                                    <pre class="pre-tags" style="font-weight: initial" id="message">Thành công</pre>
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
                        }else if (submission.status && submission.status.id === 4) {
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

                            count=count+1;
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
                        }
                        else {

                            var stdout = decode(submission.stdout);
                            var compile_output = decode(submission.compile_output);
                            var output = [compile_output, stdout].join("\n").trim();

                            let submission_add_to_list = {
                                executionTime: submission.time,
                                memory: submission.memory,
                                my_output: submission.compile_output,
                                expected_output: submission.expected_output,
                                stdin: submission.stdin,
                                ispassed: false
                            };
                            submissions_to_send.push(submission_add_to_list);


                            console.log("status ID: "+submission.status.id);
                            count=count+1;
                            newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                   <span>${submission.memory} KB</span>
                                                   <span>Lỗi biên dịch</span>
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

                       /* // Các thông tin cần gửi lên server
                        const submissionsToSend = result.submissions.map(submission => {
                            // Tạo đối tượng submission cho mỗi test case
                            return {
                                executionTime: submission.time,
                                memory: submission.memory,
                                my_output: (submission.stdout)
                            };
                        });*/
                        console.log("source_code sent: " + JSON.stringify(encode(source_code)));
                        console.log("source_code sent: " + JSON.stringify(submissions_to_send));
                        try {
                            const response = await fetch('/api/submissions/add', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify({
                                    assignment_id: assignment_id,
                                    sourceCode: encode(source_code),
                                    language: langagueName,
                                    testCaseListDTOS: submissions_to_send
                                })
                            });

                            const result = await response.json();
                            console.log("Submission return: " + JSON.stringify(result));

                        } catch (error) {
                            console.error("error when call /api/submit: "+ error);
                        }



                }
            };

            // Bắt đầu kiểm tra trạng thái
            await checkSubmitStatus();
        } catch (error) {
            console.error(error);
        }

});