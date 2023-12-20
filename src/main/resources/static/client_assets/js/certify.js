function getLevelClass(levelId) {
    if (levelId === 1) {
        return "green";
    } else if (levelId === 2) {
        return "yellow";
    } else if (levelId === 3) {
        return "redH";
    } else {
        return "";
    }
}
function notFoundResultCertify(){
    let result = " <div class=\"course__sidebar-widget grey-bg\">\n" +
        "                            <div class=\"course__sidebar-info\">\n" +
        "                                <h3 class=\"course__sidebar-title\">KHÔNG TÌM THẤY CHỨNG CHỈ</h3>\n" +
        "                            </div>\n" +
        "                        </div>"
    $('#assignment_kits').append(result);
}
function certifyContent(assignment_kit){
    let languageName = assignment_kit.language ? assignment_kit.language.name : "Algorithm";
    let assignment_kitRow =`                              <div class="col-xxl-6 col-xl-6 col-lg-6 col-md-6" th:each="assignment_kit : ${assignment_kits}">
                                 <div class="course__item white-bg mb-30 fix">
                                    <div class="course__thumb w-img p-relative fix">
                                          <div class="course__tag">
                                               <a class="sky-blue">${languageName}</a>
                                          </div>
                                    </div>
                                    <div class="course__content">
                                       <div class="course__meta d-flex align-items-center justify-content-between">
                                          <div class="course__lesson">
                                             <span><i class="far fa-book-alt"></i> ${assignment_kit.time} phút</span>
                                          </div>
                                          <div class="course__rating">
                                             <span><i style="color: #53545b;" class='bx bx-user'></i>${assignment_kit.totalParticipants} tham gia</span>
                                          </div>
                                       </div>
                                       <h3 style="margin-bottom: 0; margin-top: 15px;text-transform: uppercase; text-align: center" class="course__title"><a style="font-size: 22px" href="/skills-verification/details/${assignment_kit.id}">${assignment_kit.title}</a></h3>
                                      <div class="mt-20">
                                         <p>Số bài tập: ${assignment_kit.numberOfAssignment}</p>
                                         <p style="margin-bottom: 0 !important;">Số câu hỏi trắc nghiệm: ${assignment_kit.numberOfQuiz}</p>
                                      </div>
                                    </div>
                                    <div class="course__more d-flex justify-content-between align-items-center">
                                       <div class="course__status">
                                          <span class="${getLevelClass(assignment_kit.level.id)}">${assignment_kit.level.name}</span>
                                       </div>
                                       <div class="course__btn">
                                          <a href="/skills-verification/details/${assignment_kit.id}" class="link-btn">
                                             Xem chi tiết
                                             <i class="far fa-arrow-right"></i>
                                             <i class="far fa-arrow-right"></i>
                                          </a>
                                       </div>
                                    </div>
                                 </div>
                              </div>`
    $('#assignment_kits').append(assignment_kitRow);
}