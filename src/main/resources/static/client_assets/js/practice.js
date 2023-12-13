
    function decode(bytes) {
    var escaped = escape(atob(bytes || ""));
    return decodeURIComponent(escaped);

}

    function timeUpdate(subID, time) {
    // Lấy thẻ span theo id
    const timeSpan = document.getElementById('timeSpan-' + subID);
    timeSpan.innerHTML = '<i class="icon_clock_alt"></i>    ' + moment(time).fromNow();
}

    function timeUpdateCommentTime(id, time) {
    // Lấy thẻ span theo id
    const timeSpan = document.getElementById('commentSpan-' + id);
    timeSpan.innerHTML = '<i class="icon_clock_alt"></i>    ' + moment(time).fromNow();
}

    function decodeSubmission(source, id) {
    var codeTag = document.getElementById('src_submission-' + id);
    codeTag.classList.add('language-java')
    codeTag.innerHTML = decode(source);
}
    function viewMoreEvent(id) {
    const viewMoreButton = document.getElementById('view-more-submission-button-'+id);
    const testcaseDetails = document.getElementById('submission-details-'+id);
    viewMoreButton.addEventListener('click', () => {
    if (testcaseDetails.style.display === 'block') {
    testcaseDetails.style.display = 'none';
} else {
    testcaseDetails.style.display = 'block';
}
});
}

