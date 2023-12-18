$(document).ready(function () {
    var maxInputs = 12;

    $('.tdContentInput').on('keydown', function (event) {
        if (event.key === 'Enter' || event.key === 'Tab') {
            addNewInput();
        }
    });

    $(document).on('keydown', '.tdContentInput', function (event) {
        if (event.key === 'Enter' || event.key === 'Tab') {
            addNewInput();
        }
    });

    function addNewInput() {
        var inputValue = $('.tdContentInput:last').val();

        if (inputValue.trim() !== '') {
            if ($('.tdContentInput').length < maxInputs) {
                var newInput = $('<input type="text" class="tdContentInput" placeholder="할일 추가(Enter 또는 Tab) / 최대 60자">');
                $('.tdContentInput:last').after(newInput);

                newInput.focus();
            }
        }
    }

    $('#todoBorder1').click(function () {
        $('#nav1').css('color', 'green');
        $('#nav2').css('color', 'black');
    });

    $('#todoBorder2').click(function () {
        $('#nav2').css('color', 'green');
        $('#nav1').css('color', 'black');
    });

    $('#nav1').click(function () {
        $('#todoBorder1').css('border-color', '#00c473');
        $('#todoBorder2').css('border-color', '#d3d3d3');
        $('#nav1').css('color', '#00c473');
        $('#nav2').css('color', '#d3d3d3');
        $('#tab1').css('display','block');
        $('#tab2').css('display','none');
    });

    $('#nav2').click(function () {
        $('#todoBorder2').css('border-color', '#00c473');
        $('#todoBorder1').css('border-color', '#d3d3d3');
        $('#nav2').css('color', '#00c473');
        $('#nav1').css('color', '#d3d3d3');
        $('#tab1').css('display','none');
        $('#tab2').css('display','block');
    });


    // $('.bx-checkbox').click(function () {
    //     var todoList = $(this).closest('.todoList');
    //     todoList.find('.bx-checkbox-checked').css('display', 'block');
    //     todoList.find('.bx-checkbox').css('display', 'none');
    //     todoList.find('.tdContentP').css('text-decoration', 'line-through');
    //     todoList.find('.tdContentP').css('color', 'lightgray');
    //     updateProgressBar();
    // });
    //
    // $('.bx-checkbox-checked').click(function () {
    //     var todoList = $(this).closest('.todoList');
    //     todoList.find('.bx-checkbox-checked').css('display', 'none');
    //     todoList.find('.bx-checkbox').css('display', 'block');
    //     todoList.find('.tdContentP').css('text-decoration', 'none');
    //     todoList.find('.tdContentP').css('color', 'black');
    //     updateProgressBar();
    // });

    function updateProgressBar() {
        var totalTodos = $('.todoList').length;
        var completedTodos = $('.bx-checkbox-checked:visible').length;
        var progress = (completedTodos / totalTodos) * 100;

        // Update the progress bar width
        $('#progressBar').css('width', progress + '%');
        $('.tdCnt').text(completedTodos + '/' + totalTodos);
        $('.tdPercent').text(percentage.toFixed(0) + '%');
    }

});

