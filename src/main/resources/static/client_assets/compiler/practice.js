
const resultSection = document.getElementById('result');
var run = document.getElementById("run")
var submit = document.getElementById("submit")
var theme = document.getElementById("theme")
const excuting = document.getElementById('excuting');
const testCasesElement = document.getElementById('testCases');
const testCasesJSON = testCasesElement.getAttribute('data-test-cases');
const testCasesContainer = document.getElementById('test-cases-container');

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

run.addEventListener("click", async function () {
    const testCases = JSON.parse(testCasesJSON); // Giả sử testCasesJSON chứa danh sách các test case dưới dạng JSON.
    const language =option;
    const source = editor.getValue();
    excuting.style.display = 'block';
    const bottomElement = document.body;
    bottomElement.scrollIntoView({ behavior: 'smooth' });
    let count =0;
    // Lặp qua từng TestCase
    for (const testCase of testCases) {
        const postURL = 'https://judge0-ce.p.rapidapi.com/submissions?base64_encoded=false&fields=*';
        const options = {
            method: 'POST',
            headers: {
                'X-RapidAPI-Key': MY_API_KEY,
                'X-RapidAPI-Host': 'judge0-ce.p.rapidapi.com'
            },
            body: new URLSearchParams({
                language_id: language,
                source_code: source,
                stdin: testCase.input,
                expected_output: testCase.expectedOutput
            })
        };

        try {
            const response = await fetch(postURL, options);
            const postResult = await response.json();
            console.log("token: "+ postResult.token);
            const checkStatus = async () => {
                const getURL = 'https://judge0-ce.p.rapidapi.com/submissions/' + postResult.token + '/?base64_encoded=false&fields=*';
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

                const newContainer = document.createElement('div');
                newContainer.classList.add('col-xxl-10', 'offset-xxl-1', 'col-xl-10', 'offset-xl-1', 'col-lg-10', 'offset-lg-1');

                if (result.status && result.status.id <= 2) {
                    //"Processing" - "In Queue"
                    setTimeout(checkStatus, 4000);
                } else if (result.status && result.status.id === 3) {
                    //Accepted
                    count=count+1;
                    newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${result.time}s</span>
                                                   <span>${result.memory}KB</span>
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
                                    <pre class="pre-tags" id="input">${result.stdin}</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Your Output</h4>
                                    <pre class="pre-tags" id="my_output">${result.stdout}</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Expected Output</h4>
                                    <pre class="pre-tags" id="expected_output">${result.expected_output}</pre>
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
                }
                else if (result.status && result.status.id === 4) {
                    //Wrong Answer
                    count=count+1;
                    newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${result.time}s</span>
                                                   <span>${result.memory} KB</span>
                                                   <span>Sai kết quả</span>
                </div>
                <h3 class="events__title">
                    <a class="price__features">
                        <i class="far fa-warning" style="background: rgb(48 168 32 / 9%);color: #d12a47;
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
                                    <pre class="pre-tags" id="input">${result.stdin}</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Your Output</h4>
                                    <pre class="pre-tags" id="my_output">${result.stdout}</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Expected Output</h4>
                                    <pre class="pre-tags" id="expected_output">${result.expected_output}</pre>
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
                }
                else {
                    console.log("status ID: "+result.status.id);
                    output.value = "Xảy ra lổi trong quá trình biên dịch";
                }
                const viewMoreButton = document.getElementById(`view-more-button-${count}`);
                const testcaseDetails = document.getElementById(`event-details-${count}`);

// Thêm sự kiện "click" vào nút "View More"
                viewMoreButton.addEventListener('click', () => {
                    if (testcaseDetails.style.display === 'block') {
                        testcaseDetails.style.display = 'none';
                    } else {
                        testcaseDetails.style.display = 'block';
                    }
                });
                excuting.style.display = 'none';
                resultSection.style.display = 'block';
            };

            // Bắt đầu kiểm tra trạng thái
            checkStatus();
        } catch (error) {
            console.error(error);
        }
    }
});
