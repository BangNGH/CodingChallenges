import {MY_CHATGPT_API_KEY} from "./config.js"
var run = document.getElementById("run");
var theme = document.getElementById("theme");
const excuting = document.getElementById('excuting');
const testCasesContainer = document.getElementById('test-cases-container');
const resultSection = document.getElementById('result');
const inputElement = document.getElementById('input');
$(document).ready(function () {
    editor.on('change', function () {
        saveContent(editor.getValue().trim());
    });
    $.ajax({
        type: 'GET',
        url: '/api/submissions/get-content',
        success: function (response) {
            if (response!=null) {
                const dataJson = JSON.parse(response);
                const mode_receive = dataJson.mode;
                const content_receive = dataJson.content;
                const language_name_receive = dataJson.language_name;
                const current_input_value = 'input.dd-option-value[value="' + mode_receive + '"]';
                var input_to_change = document.querySelector(current_input_value);
                var selectedInput = document.querySelector('a.dd-option-selected');
                var dSelectedValue = document.querySelector('input.dd-selected-value');
                var dSelectedText = document.querySelector('label.dd-selected-text');
                if (mode_receive === "text/x-c++src") {
                    dSelectedText.innerHTML = "C++";
                }
                if (mode_receive === "text/x-java") {
                    dSelectedText.innerHTML = "Java";
                }
                if (mode_receive === "text/x-csharp") {
                    dSelectedText.innerHTML = "C#";
                }
                if (mode_receive === "text/x-python") {
                    dSelectedText.innerHTML = "Python";
                }

                if (input_to_change) {
                    var parentA = input_to_change.closest('a');
                    // Kiểm tra xem có thẻ a được tìm thấy không
                    if (parentA) {
                        dSelectedValue.value = mode_receive;
                        selectedInput.classList.remove('dd-option-selected');
                        // Xoá class "dd-option-selected" khỏi thẻ a
                        parentA.classList.add('dd-option-selected');
                        console.log('Đã add class "dd-option-selected" từ thẻ a.');
                    } else {
                        console.log('Không tìm thấy thẻ a chứa input.');
                    }
                } else {
                    console.log('Không tìm thấy thẻ input có giá trị là "input_value".');
                }

                editor.setOption("mode", mode_receive);
                console.log('content_receive');
                editor.setValue(content_receive);
                if (mode_receive === "text/x-c++src") {
                    option = 54;
                }
                if (mode_receive === "text/x-java") {
                    option = 91;

                }
                if (mode_receive === "text/x-csharp") {
                    option = 51;
                }
                if (mode_receive === "text/x-python") {
                    option = 71;
                }
                langague_name = language_name_receive;
            }
        },
        error: function (error) {
            console.error('Error getting content:', error);
        }
    });
});

function saveContent(content) {
    // Lấy thẻ input đầu tiên có class "dd-option-selected" trong thẻ a
    var selectedInput = document.querySelector('a.dd-option-selected input.dd-option-value');
    $.ajax({
        type: 'POST',
        url: '/api/submissions/save-content',
        data: {
            content: content,
            mode: selectedInput.value,
            langague_name: langague_name,
            current_tab_id: "-1"
        },
        success: function (response) {
            console.log('Content saved successfully!');
        },
        error: function (error) {
            console.error('Error saving content:', error);
        }
    });
}

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
import {MY_API_KEY} from "./config.js";


//batch submission
run.addEventListener("click", async function () {
    testCasesContainer.innerHTML='';
    excuting.style.display = 'block';
    const bottomElement = document.getElementById('excuting');
    bottomElement.scrollIntoView({behavior: 'smooth'});
    let count = 0;
    let submissions;
    let language_server;
    if (option==91){
        language_server="java";
    }else if(option==51){
        language_server="csharp";
    }else{
        language_server="null";
    }
    submissions = [{
            language: language_server,
            stdin: inputElement.value,
            source_code: editor.getValue().trim()

    }];

    console.log(submissions);

    const postURL = 'http://localhost:9999/submissions/batch';
    const options = {
        method: 'POST',
        headers: {
            'content-type': 'application/json',
        },
        body: JSON.stringify({
            submissions: submissions
        })
    };

    try {
        const response = await fetch(postURL, options);
        const postResult = await response.json();
        let submissions=[];
        submissions=(postResult.submissions);
        console.log(submissions);

        submissions.every((submission, index) => {
            console.log(submission.status);
            const newContainer = document.createElement('div');
            newContainer.classList.add('col-xxl-10', 'offset-xxl-1', 'col-xl-10', 'offset-xl-1', 'col-lg-10', 'offset-lg-1');
            if (submission.status === "1") {
                //Accepted
                var stdin = decode(submission.stdin).trim();
                var stdout = decode(submission.stdout).trim();
                count = count + 1;
                let isSampleTestCase;
                isSampleTestCase = true;
                newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
                  <div class="events__content">
                        <div class="events__meta">
                            <span>${submission.time}s</span>
                       
                            <span>Success</span>
                        </div>
                        <h3 class="events__title">
                            <a class="price__features">
                                <i class="far fa-check" style="background: rgb(48 168 32 / 9%);
                                    border-radius: 50%;
                                    font-size: 25px;"></i> Sample Test case 0${count}
                            </a>
                        </h3>
                  </div>
                 <div class="events__more">
                    ${isSampleTestCase ? `<a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
                                Xem chi tiết
                                <i class="far fa-arrow-right"></i>
                                <i class="far fa-arrow-right"></i>
                              </a>` : `<a class="link-btn" id="view-more-button-${count}" style="cursor: pointer">
                                          Hidden TestCase
                                          <i style="color: #69b35c;" class="far fa-unlock"></i>
                                          <i style="color: #e65972;" class="far fa-lock"></i>
                                       </a>`}
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
                                    <h4 class="blue-grey">Trạng Thái</h4>
                                    <pre class="pre-tags" style="font-weight: initial; text-align: center">Thành công</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Input</h4>
                                     <pre class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${stdin}
                                       </code>
                                    </pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Output</h4>
                                     <pre class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${stdout}
                                       </code>
                                    </pre>
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
                Prism.highlightAll();
                resultSection.style.display = 'block';

            } else if (submission.status === "2") {
                //Wrong Answer
                var stdin = decode(submission.stdin).trim();
                var stdout = decode(submission.stdout).trim();
                var expected_output = decode(submission.expected_output).trim();

                count = count + 1;
                newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                 
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
                                    <h4 class="blue-grey">Trạng Thái</h4>
                                    <pre class="pre-tags" style="font-weight: initial;text-align: center">Kết quả không trùng khớp</pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Input</h4>
                                  
                                   <pre class="pre-tags line-numbers language-markup">
                                      <code class="language-markup"> 
                                        ${stdin}
                                       </code>
                                     
                                    </pre>
                                
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Your Output</h4>
                                    <pre class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${stdout}
                                       </code>
                                    </pre>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="contact__info-item d-flex align-items-start mb-20">
                                <div class="contact__info-text" style="width: 100%">
                                    <h4 class="blue-grey">Expected Output</h4>
                               
                                     <pre class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${expected_output}
                                       </code>
                                    </pre>
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
                Prism.highlightAll();
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
                console.log("status ID: " + submission.status);
                count = count + 1;
                newContainer.innerHTML = `
        <div class="events__item mb-10 hover__active">
              <div class="events__item-inner d-sm-flex align-items-center justify-content-between white-bg">
            <div class="events__content">
                <div class="events__meta">
                   <span>${submission.time}s</span>
                                                 
                                                  <span>Error Status ${submission.status}</span>
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
                                    <h4 class="blue-grey">Trạng Thái</h4>
                              
                                  <pre class="pre-tags line-numbers language-markup">
                                       <code class="language-markup">
                                         ${stdout}
                                       </code>
                                    </pre>
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
                Prism.highlightAll();

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
    } catch (error) {
        console.error(error);
    }
});
