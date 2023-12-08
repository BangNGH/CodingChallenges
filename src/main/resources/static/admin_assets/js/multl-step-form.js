$(document).ready(function () {
    $(".next").click(function () {
        var selectBox = document.getElementById("selectBox");
        if (selectBox.value !== "") {

            const hiddenDiv = document.getElementById("hidden_value");
            const languageInputElement = document.getElementById("language_option");

            if (languageInputElement !== null) {
                languageInputElement.setAttribute("value", selectBox.value);
            } else {
                const createInputElementLanguage = document.createElement("input");
                createInputElementLanguage.setAttribute("value", selectBox.value);
                createInputElementLanguage.setAttribute("name", "language_option");
                createInputElementLanguage.setAttribute("id", "language_option");
                createInputElementLanguage.setAttribute("type", "hidden");
                hiddenDiv.appendChild(createInputElementLanguage);
            }

            const inputElement = document.getElementById("markdown_content");
            if (inputElement !== null) {
                if (markdown_editor.value() === "") {
                    inputElement.setAttribute("value", "This assignment has no solution");
                }else {
                    inputElement.setAttribute("value", markdown_editor.value());
                }
            } else {
                const createInputElement = document.createElement("input");
                if (markdown_editor.value() !== "") {
                    createInputElement.setAttribute("value", markdown_editor.value());
                } else {
                    createInputElement.setAttribute("value", "This assignment has no solution");
                }
                createInputElement.setAttribute("name", "markdown_content");
                createInputElement.setAttribute("id", "markdown_content");
                createInputElement.setAttribute("type", "hidden");
                hiddenDiv.appendChild(createInputElement);
            }
        }
        //validation
        var form = $('#msform');
        form.validate({
            errorElement: 'span',
            errorClass: 'help-block',
            highlight: function (element, errorClass, validClass) {
                $(element).closest('.form-validate').addClass("has-error");
            },
            unhighlight: function (element, errorClass, validClass) {
                $(element).closest('.form-validate').removeClass("has-error");
            },
            rules: {
                title: {
                    required: true,
                },
                score: {
                    required: true,
                    number: true,
                },
                input: {
                    required: true,
                },
                output: {
                    required: true,
                },
                name: {
                    required: true,
                },
                "TSName[]": {
                    required: true,
                },
                "TSScore[]": {
                    number: true,
                    required: true,
                },
                "TSInput[]": {
                    required: true,
                },
                "TSOutput[]": {
                    required: true,
                },
                timeLimit: {
                    number: true,
                    required: false,
                },
                memoryLimit: {
                    number: true,
                    required: false,
                }
            },
            messages: {
                title: {
                    required: "Question name required",
                },
                score: {
                    required: "Score required",
                    number: "Please enter a valid number for the score",
                },
                input: {
                    required: "Input required",
                },
                output: {
                    required: "Output required",
                },
                name: {
                    required: "Test case name required"
                },
                timeLimit: {
                    number: "Please enter a valid number for the time limit",
                },
                memoryLimit: {
                    number: "Please enter a valid number for the memory limit and greater than 2048",
                },
                "TSName[]": {
                    required: "Question name required",
                },
                "TSScore[]": {
                    required: "Score required",
                    number: "Please enter a valid number for the score",
                },
                "TSInput[]": {
                    required: "Input required",
                },
                "TSOutput[]": {
                    required: "Output required",
                }
            }
        });
        if (form.valid() === true) {
            var current_fs, next_fs, previous_fs; //fieldsets
            var opacity;

            current_fs = $(this).parent();
            next_fs = $(this).parent().next();
            //Add Class Active
            $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

            //show the next fieldset
            next_fs.show();
            //hide the current fieldset with style
            current_fs.animate({opacity: 0}, {
                step: function (now) {
                    // for making fielset appear animation
                    opacity = 1 - now;

                    current_fs.css({
                        'display': 'none',
                        'position': 'relative'
                    });
                    next_fs.css({'opacity': opacity});
                },
                duration: 600
            });
        }
    });

    $(".previous").click(function () {
        current_fs = $(this).parent();
        previous_fs = $(this).parent().prev();

        //Remove class active
        $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

        //show the previous fieldset
        previous_fs.show();

        //hide the current fieldset with style
        current_fs.animate({opacity: 0}, {
            step: function (now) {
                // for making fielset appear animation
                opacity = 1 - now;

                current_fs.css({
                    'display': 'none',
                    'position': 'relative'
                });
                previous_fs.css({'opacity': opacity});
            },
            duration: 600
        });
    });

    $('.radio-group .radio').click(function () {
        $(this).parent().find('.radio').removeClass('selected');
        $(this).addClass('selected');
    });

    $(".submit").click(function () {
        return false;
    })
    var nextedu = 0;
    var number_testcase = parseInt($('#number_testcase').val());
    if (!isNaN(number_testcase)) {
        nextedu = number_testcase;
    }

    $("#add-more").click(function (e) {
        e.preventDefault();
        var addto = "#field" + nextedu;
        var addRemove = "#field" + (nextedu);
        nextedu = nextedu + 1;

        var newIn = ' <div id="field' + nextedu + '">\n' +
            '                                            <div class="form-group row">\n' +
            '                                                <div class="col">\n' +
            '                                                    <label>Name:<span class="text-danger">*</span></label>\n' +
            '                                                    <input style="margin-bottom: unset" name="TSName[]" class="form-validate" placeholder="Testcase name...">\n' +
            '                                                </div>\n' +
            '                                                <div class="col">\n' +
            '                                                    <label>Score:<span class="text-danger">*</span></label>\n' +
            '                                                    <input style="margin-bottom: unset" name="TSScore[]" class="form-validate" placeholder="Score...">\n' +
            '                                                </div>\n' +
            '                                            </div>\n' +
            '                                            <div class="form-group row">\n' +
            '                                                <div class="col">\n' +
            '                                                    <label>Input:<span class="text-danger">*</span></label>\n' +
            '                                                    <textarea style="margin-bottom: unset; height: 140px" name="TSInput[]" class="form-validate" placeholder="Input..."></textarea>\n' +
            '                                                </div>\n' +
            '                                                <div class="col">\n' +
            '                                                    <label>Ouput:<span class="text-danger">*</span></label>\n' +
            '                                                    <textarea style="margin-bottom: unset; height: 140px" name="TSOutput[]" class="form-validate" placeholder="Output..."></textarea>\n' +
            '                                                </div>\n' +
            '                                            </div>\n' +
            '                                            <div class="form-group">\n' +
            '                                                 <input name="check[]"  value="true" style="width: unset; margin-bottom: unset; cursor: pointer" type="checkbox" checked class="iCheck-flat-green">\n' +
            '                                                <label class="control-label">Mark as sample test case</label>\n' +
            '                                            </div>\n' +
            '                                        </div>';
        var newInput = $(newIn);

        var removeBtn = '<button style="margin-bottom: 20px" id="remove' + (nextedu - 1) + '" class="btn btn-danger remove-me" >Remove</button>';
        var removeButton = $(removeBtn);
        $(addto).after(newInput);
        $(addRemove).after(removeButton);
        $("#field" + nextedu).attr('data-source', $(addto).attr('data-source'));
        $("#count").val(nextedu);

        $('.remove-me').click(function (e) {
            e.preventDefault();
            var fieldNum = this.id.charAt(this.id.length - 1);
            var fieldID = "#field" + fieldNum;
            $(this).remove();
            $(fieldID).remove();

        });
    });
});