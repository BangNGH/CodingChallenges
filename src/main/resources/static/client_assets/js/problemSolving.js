function assignmentContent(assignment){

    let assignmentRow = `                                    <div class="col-xxl-6 col-xl-6 col-lg-6 col-md-6">
                                        <div class="course__item white-bg mb-30 fix">
                                            <div class="course__thumb w-img p-relative fix">
                                                <a>
                                                </a>
                                                <div class="course__tag">
                                                    <a class="yellow" href="#">Algorithm</a>
                                                </div>
                                            </div>
                                            <div class="course__content">
                                                <div class="course__meta d-flex align-items-center justify-content-between">
                                                    <div class="course__lesson">
                                                        <span><i class="far fa-book-alt"></i>Điểm tối đa: ${assignment.max_score} </span>
                                                    </div>
                                                    <div class="course__rating">
                                                        <span><i class="icon_star"></i>Tỉ lệ thành công: ${assignment.successRate}%</span>
                                                    </div>
                                                </div>
                                                <h3 style="margin-top: 20px; font-size: 23px !important;" class="course__title"><a href="/problemSolving/${assignment.id}">${assignment.title}</a></h3>
                                                <div class="course__teacher d-flex align-items-center">
                                                    <div class="course__teacher-thumb mr-15">
                                                        <img src="${assignment.lecturer.imagesPath ? assignment.lecturer.imagesPath : 'https://bootdey.com/img/Content/avatar/avatar1.png'}"/>

                                                    </div>
                                                    <h6><a href="problemSolving/${assignment.id}">${assignment.lecturer.fullName}</a></h6>
                                                </div>
                                            </div>
                                            <div class="course__more d-flex justify-content-between align-items-center">
                                                <div class="course__status">
                                                    <span class="${getLevelClass(assignment.level.id)}">${assignment.level.name}</span>
                                                </div>
                                                <div class="course__btn">
                                                    <a href="/problemSolving/${assignment.id}" class="link-btn">
                                                        Giải bài tập
                                                        <i class="far fa-arrow-right"></i>
                                                        <i class="far fa-arrow-right"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>`;
    $('#assignments').append(assignmentRow);
}
function getLevelClass(levelId) {
    if (levelId === 1) {
        return "green";
    } else if (levelId === 2) {
        return "blue";
    } else if (levelId === 3) {
        return "redH";
    } else {
        return "";
    }
}
function buildPagination(response) {
    if (response.totalPages > 1) {
        let paginationRow = `
        <div id="page-fetch" class="col-xxl-12">
            <div class="basic-pagination wow fadeInUp mt-30" data-wow-delay=".2s">
                <ul class="d-flex align-items-center">
                    <li class="prev">
                        <a style="cursor: pointer" class="link-btn link-prev" ${(response.number > 0) ? `data-page="${response.number - 1}"` : ''}>
                            Prev
                            <i class="arrow_left"></i>
                             <i class="arrow_left"></i>
                        </a>
                    </li>
    `;
        for (let i = 1; i <= response.totalPages; i++) {
            paginationRow += `
            <li>
                <a style="cursor: pointer" ${(response.number != i-1) ? `data-page="${i-1}"` : ''}
                ${response.number === i-1 ? 'class="current-page"' : ''}>${i}</a>
            </li>
        `;
        }
        paginationRow += `
                    <li class="next">
                        <a class="link-btn" style="cursor: pointer" ${(response.number < response.totalPages-1) ? `data-page="${response.number + 1}"` : ''}>
                            Next
                            <i class="arrow_right"></i>
                            <i class="arrow_right"></i>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    `;
        $('#pagination').append(paginationRow);
    }
}
function buildPaginationForSearch(response) {
    if (response.totalPages > 1) {
        let paginationRow = `<div id="page-searchh" class="col-xxl-12">
            <div class="basic-pagination wow fadeInUp mt-30" data-wow-delay=".2s">
                <ul class="d-flex align-items-center">
                    <li class="prev">
                        <a style="cursor: pointer" class="link-btn link-prev" ${(response.number > 0) ? `data-page="${response.number - 1}"` : ''}>
                            Prev
                            <i class="arrow_left"></i>
                             <i class="arrow_left"></i>
                        </a>
                    </li>
    `;
        for (let i = 1; i <= response.totalPages; i++) {
            paginationRow += `
            <li>
                <a style="cursor: pointer" ${(response.number != i-1) ? `data-page="${i-1}"` : ''}
                ${response.number === i-1 ? 'class="current-page"' : ''}>${i}</a>
            </li>
        `;
        }
        paginationRow += `
                    <li class="next">
                        <a class="link-btn" style="cursor: pointer" ${(response.number < response.totalPages-1) ? `data-page="${response.number + 1}"` : ''}>
                            Next
                            <i class="arrow_right"></i>
                            <i class="arrow_right"></i>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    `;
        $('#pagination').append(paginationRow);
    }
}
function buildPaginationForFilter(response) {
    if (response.totalPages > 1) {
        let paginationRow = `<div id="page-filter" class="col-xxl-12">
            <div class="basic-pagination wow fadeInUp mt-30" data-wow-delay=".2s">
                <ul class="d-flex align-items-center">
                    <li class="prev">
                        <a style="cursor: pointer" class="link-btn link-prev" ${(response.number > 0) ? `data-page="${response.number - 1}"` : ''}>
                            Prev
                            <i class="arrow_left"></i>
                             <i class="arrow_left"></i>
                        </a>
                    </li>
    `;
        for (let i = 1; i <= response.totalPages; i++) {
            paginationRow += `
            <li>
                <a style="cursor: pointer" ${(response.number != i-1) ? `data-page="${i-1}"` : ''}
                ${response.number === i-1 ? 'class="current-page"' : ''}>${i}</a>
            </li>
        `;
        }
        paginationRow += `
                    <li class="next">
                        <a class="link-btn" style="cursor: pointer" ${(response.number < response.totalPages-1) ? `data-page="${response.number + 1}"` : ''}>
                            Next
                            <i class="arrow_right"></i>
                            <i class="arrow_right"></i>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    `;
        $('#pagination').append(paginationRow);
    }
}