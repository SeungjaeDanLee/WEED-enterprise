$(document).ready(function(){
    var info = {}; // info 변수를 빈 객체로 초기화

//모달작성
    var calendarEl = document.getElementById('calendar');
    var eventModal = document.getElementById('eventModal'); //모달 창 띄우기
    var eventTitleInput = document.getElementById('eventTitle');
    var eventContentInput = document.getElementById('eventContent');

// 날짜인풋
    var startDateInput = document.getElementById('startDate');
    var endDateInput = document.getElementById('endDate');

//사용자 지정 이벤트 색깔
    var selectedColor = "#7A9DA8"; //색깔 초기값설정
    var eventColorInput  = document.getElementById('eventcolor');

//모달 작성 버튼
    var saveEventButton = document.getElementById('saveEvent');
    var closeModalButton = document.getElementById('closeModalsell');

//모달 상세
    var eventModalDetail = document.getElementById('eventModalDetail');    //상세모달창
    var eventTitleDetail = document.getElementById('eventTitleDetail');
    var startDateDetail = document.getElementById('startDateDetail');
    var endDateDetail = document.getElementById('endDateDetail');
    var eventContentDetail = document.getElementById('eventContentDetail');

//모달 상세 버튼
    var closeModalButtonDetail = document.getElementById('closeModalDetail');
    var deleteModalButtonDetail =document.getElementById('deleteModalDetail');


//모달 수정
    var ModifyModal = document.getElementById('modifyModal'); //원래정보를 갖고있는 모달
    var ModifyTitle = document.getElementById('modifyTitle');
    var ModifyStartDate=document.getElementById('modifyStartDate');
    var ModifyEndDate=document.getElementById('modifyEndDate');
    var ModifyColor=document.getElementById('modifycolor');
    var ModifyContent = document.getElementById('modifyContent');

    var ModifyButton = document.getElementById('ModifyButton');
    var ModifyCloseButton = document.getElementById('modifycloseModal');
    var ModifySaveButton = document.getElementById('modifysaveButton');

    var clickCount = 0;
    var timeout;

    var title = eventTitleInput.value.trim();
    var content = eventContentInput.value.trim();
// if (!title) return; // 타이틀이 비어있으면 아무 동작도 하지 않음


// 클라이언트(날짜클릭을 기준으로 받은 데이터값)
    var startDateInputValue = startDateInput.value;
    var endDateInputValue = endDateInput.value;

// 사용자가 선택한 색상 가져오기
    selectedColor = eventColorInput.value;

// 이벤트 ID 생성
    var eventId = generateEventId();

    // click한 이벤트의 id값
    var clickedDefId = -1;
    var clickedEventId = -1;

    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        // 달력 설정
        headerToolbar: {
            left: 'prev',
            center: 'title',
            right: 'next today'
        },

        allDay: true, // 기본값을 하루 종일로 설정

        //이벤트스타일 설정
        eventContent: function (info) {
            // 이벤트의 콘텐츠를 생성하는 함수
            var content = document.createElement('div');
            content.classList.add('event-content');

            // 이벤트의 제목과 기타 정보를 추가
            content.innerHTML = '<b>' + info.event.title + '</b><br>';

            return { domNodes: [content] };
        },

        timeZone: 'Asia/Seoul',
        locale: "ko",   // 언어설정
        dayMaxEvents: 2, // 하나의 날짜에 표시할 수 있는 최대 이벤트 수
        eventDisplay: 'block',  //단일이벤트도 블럭형태로 표시


        // 날짜 '일' 없애기
        dayCellContent: function (info) {
            var number = document.createElement("a");
            number.classList.add("fc-daygrid-day-number");
            number.innerHTML = info.dayNumberText.replace("일", '');

            if (info.view.type === "dayGridMonth") {
                return {
                    html: number.outerHTML
                };
            }

            return {
                domNodes: []
            };
        },

        selectable: true,
        editable: true, // 일정 이동 및 기간수정 가능하도록 활성화
        eventResizableFromStart: true,


        select: function (info) {
            eventModal.classList.add('show');
            // startDateInput.value = formatDate(info.start);
            // endDateInput.value = formatDate(info.end);
            startDateInput.value = info.start;
            endDateInput.value = info.end;

            // select 이벤트에서 info.id 설정
            info.id = generateEventId();
            info.date = info.start;
            info.allDay = info.allDay;

        },

        // ===================================날짜클릭시 모달창(이벤트 저장 및 등록버튼과 이어짐)===================================
        dateClick: function (eventInfo) {
            closeAllModals();

            clickCount++;
            info = eventInfo;

            // 더블 클릭에 따른 이벤트 처리
            if (clickCount === 1) {
                timeout = setTimeout(function () {
                    clickCount = 0; // 클릭 횟수 초기화
                }, 300);  // 300ms 내에 클릭하지 않으면 초기화
            } else if (clickCount === 2) {
                clearTimeout(timeout);
                clickCount = 0; // 클릭 횟수 초기화

                closeAllModals();   //다른 모달창이 나타나면 꺼짐
                eventModal.classList.add('show'); // 모달 창 나타내기

                // 더블클릭한 날짜를 시작일과 종료일로 설정
                var selectedDate = eventInfo.date;
                var startDate = new Date(selectedDate);
                var endDate = new Date(selectedDate);

                startDateInput.value = formatDate(startDate);
                endDateInput.value = formatDate(endDate);

                // startDateInput.value = startDate;
                // endDateInput.value = endDate;

                // 더블클릭 이벤트 처리
                eventTitleInput.value = ''; // 초기화
                eventContentInput.value='';

                // 클릭한 위치를 기준으로 모달창 위치 설정
                var modalLeft = eventInfo.jsEvent.clientX;
                var modalTop = eventInfo.jsEvent.clientY;

                // 모달창이 화면 하단을 넘어갈 경우 조정
                var screenHeight = window.innerHeight;
                var modalHeight = eventModal.clientHeight;


                if (modalTop + modalHeight > screenHeight) {
                    modalTop = screenHeight - modalHeight - 10;
                }

                eventModal.style.left = modalLeft + 'px';
                eventModal.style.top = modalTop + 'px';
                eventModal.style.display = 'block';

                // setTimeout을 사용하여 모달이 표시된 후에 하단설정
                setTimeout(function () {
                    var modalHeight = eventModal.clientHeight;
                    var screenHeight = window.innerHeight;

                    if (modalTop + modalHeight > screenHeight) {
                        modalTop = screenHeight - modalHeight - 10; // 10은 여유 여백
                        eventModal.style.top = modalTop + 'px';
                    }

                }, 0);

            }
        },
        // ===================================날짜클릭시 모달창(이벤트 저장 및 등록버튼과 이어짐)===================================



        // ===================================이벤트 클릭시 상세정보 보이기(상세모달과이어짐)===================================
        eventClick: function (arg) {
            clickCount++;

            // 더블 클릭에 따른 이벤트 처리
            if (clickCount === 1) {
                timeout = setTimeout(function () {
                    clickCount = 0; // 클릭 횟수 초기화
                }, 300);  // 300ms 내에 클릭하지 않으면 초기화
            } else if (clickCount === 2) {
                clearTimeout(timeout);
                clickCount = 0; // 클릭 횟수 초기화

                closeAllModals();   //다른 모달창이 나타나면 꺼짐

                // click한 이벤트의 id값
                clickedDefId = arg.event._def.defId;
                clickedEventId = arg.event.id;

                showEventinfo(arg);

                // ModifyButton을 클릭할 때 이벤트 리스너 등록
                // 수정 모달창으로 전환
                ModifyButton.addEventListener('click', function() {

                    showModifyModal(arg);
                });
            }


        },
        eventResize: function (eventResizeInfo) {
            var resizedEvent = eventResizeInfo.event;
            var newEndDate = resizedEvent.end;

            console.log('이벤트가 리사이즈되었습니다.');
            console.log('새로운 종료 날짜:', newEndDate);
        },

    });

    // 달력 그리기
    calendar.render();

    // 서버로부터 업데이트된 전체 일정을 가져와 FullCalendar에 반영
    getAllEvents();

    // 삭제 버튼을 클릭할 때 이벤트 리스너 등록
    deleteModalButtonDetail.addEventListener("click", function() {
        calendar.getEvents().forEach(function(evt) {
            if (evt._def.defId == clickedDefId) {
                deleteEvent(clickedEventId);
            }
        });
    });

    // 수정 버튼을 눌렀을 때 해당 이벤트 업데이트
    ModifySaveButton.addEventListener('click', function () {

        calendar.getEvents().forEach(function(evt) {
            if (evt._def.defId == clickedDefId) {
                handleModifySave(clickedEventId);
            }
        });
    });

    // 최초 한 번만 등록될 리스너
    // ModifySaveButton.addEventListener('click', handleModifySave);

    // 삭제 버튼 이벤트 핸들러 등록
    deleteModalButtonDetail.addEventListener('click', deleteEventCallback);

    // 수정모달 닫기
    ModifyCloseButton.addEventListener('click', function () {
        ModifyModal.style.display = 'none';
    });
    //===================================수정 모달창 띄우기===================================



    // ===================================상세정보 보이기===================================



    // ===================================이벤트 등록 및 저장기능 버튼 ===================================
    saveEventButton.addEventListener('click', function () {

        // AJAX를 이용하여 서버로 데이터 전송
        saveToServer();
        // ===================================이벤트 등록 및 저장기능 ===================================

        calendar.unselect();
        eventModal.style.display = 'none';
    });


    //날짜클릭모달 닫기
    closeModalButton.addEventListener('click' , function () {
        calendar.unselect();
        eventModal.style.display = 'none';
    });


    //일정상세닫기버튼
    closeModalButtonDetail.addEventListener('click', function () {
        calendar.unselect();
        eventModalDetail.style.display = 'none';
    });

    function generateEventId() {
        // 이벤트를 위한 고유한 ID를 생성하는 로직을 구현
        return Date.now().toString() + window.performance.now().toString().replace('.', '');
    }

    function getLoggedInMemberId() {
        //로그인된 정보 불러오기
        return new Promise(function(resolve, reject) {
            $.ajax({
                type: 'GET',
                url: '/api/current',
                success: function(response) {
                    resolve(response.id)
                },
                error: function(error) {
                    console.error('Error getting logged-in member ID:', error.responseText);
                }
            })
        });
    }

// 서버로부터 업데이트된 전체 일정을 가져와 FullCalendar에 반영
    // 기존방식
    /*
    function getAllEvents() {
        $.ajax({
            type: 'GET',
            url: '/api/events',
            success: function (response) {
                //calendar.removeAllEvents(); // 기존 이벤트 제거
                calendar.addEventSource(response); // 새로운 이벤트 소스 추가
            },
            error: function (error) {
                console.error('Failed to fetch updated events:', error);
                // 오류 처리
            }
        });
    }
    */

    // 수정된 방식
    function getAllEvents() {
        $.ajax({
            type: 'GET',
            url: '/api/events',
            success: function (response) {
                calendar.removeAllEvents(); // 기존 이벤트 제거
                calendar.addEventSource(response); // 새로운 이벤트 소스 추가
                response.forEach(function (eventData) {
                    var event = {
                        id: eventData.id,
                        title: eventData.title,
                        content: eventData.content,
                        start: new Date(eventData.start),
                        end: new Date(eventData.end),
                        color: eventData.color
                    };
                    calendar.addEvent(event);
                });

            },
            error: function (error) {
                console.error('Failed to fetch updated events:', error);
                // 오류 처리
            }
        });
    }

// 다른 모든 모달 닫기
    function closeAllModals() {
        eventModalDetail.style.display = 'none';
        ModifyModal.style.display = 'none';
        eventModal.style.display = 'none';
    }

    //삭제 구현
    function deleteEvent(clickedEventId) {
        // 클라이언트에서 이벤트 아이디 가져오기
        // var eventIdToDelete = arg.event.id;
        var eventIdToDelete = clickedEventId;

        console.log('Deleting event with ID:', eventIdToDelete); // 추가된 부분

        // 서버로 삭제 요청 보내기
        $.ajax({
            type: 'DELETE',
            url: '/api/schedule/' + eventIdToDelete + '/delete',
            success: function (deleteResponse) {
                console.log('Server response (Delete Event and Schedule):', deleteResponse);

                // 캘린더에서 이벤트 제거
                var selectedEvent = calendar.getEventById(eventIdToDelete);

                if (selectedEvent) {
                    selectedEvent.remove();
                } else {
                    console.error('Event not found with ID:', eventIdToDelete);
                }
            },
            error: function (error) {
                console.error('Error deleting event and schedule:', error.responseText);
                // 삭제 실패 시 추가 작업 수행
            }
        });

    }

//====================삭제 버튼을 눌렀을때의 이벤트 처리=========================================
// deleteModalButtonDetail에 대한 클릭 이벤트 핸들러
    function deleteEventCallback(arg) {
        console.log('Delete Event Callback Called');
        closeAllModals();
        deleteEvent(arg); // arg를 전달하여 함수 호출

        // 핸들러 등록 취소 (한 번만 실행되도록)
        deleteModalButtonDetail.removeEventListener('click', deleteEventCallback);
    }

//===================================상세 모달창 띄우기===================================
    function showEventinfo(arg) {
        closeAllModals();
        eventModalDetail.classList.add('show'); // 모달 창 나타내기

        //Ajax 요청: 스케줄 아이디를 이용해 스케줄정보 가져오기
        $.ajax({
            type: 'GET',
            url: '/api/schedule/' + arg.event.id + '/info',
            dataType: 'json',
            async:false,
            success: function (response) {
                console.log('Server response (Schedule Info):', response);
                console.log(typeof response.memberName);

                // 이벤트 등록한 멤버의 이름 및 정보표시하기
                var eventMember = document.getElementById('EventMember');
                if (response.memberName) {
                    eventMember.innerHTML = response.memberName + '님이 등록한 일정입니다';
                }

                eventTitleDetail.innerHTML = response.title;    //제목
                eventContentDetail.innerHTML = response.content;    //내용

                // 날짜 문자열을 Date 객체로 변환
                var inputStartDate = new Date(response.startDate);
                var inputEndDate = new Date(response.endDate);

                // yyyy-MM-dd 형식으로 변환
                var formattedStartDate = inputStartDate.toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' }).replace(/\./g, '').replace(/\s/g, '-');
                var formattedEndDate = inputEndDate.toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' }).replace(/\./g, '').replace(/\s/g, '-');

                startDateDetail.innerHTML = formattedStartDate;
                endDateDetail.innerHTML = formattedEndDate;


                // 클라이언트에서 현재 로그인된 멤버의 아이디 가져와서 버튼표시여부설정
                getLoggedInMemberId().then(function(loggedInMemberId) {
                    // 이벤트의 멤버 아이디와 로그인된 멤버의 아이디 비교
                    if (response.memberId === loggedInMemberId) {
                        // 로그인된 멤버와 이벤트의 멤버 아이디가 같으면 수정,삭제 버튼 보이기
                        deleteModalButtonDetail.style.display = 'block';
                        ModifyButton.style.display = 'block';

                    } else {
                        // 다를 경우 수정,삭제 버튼 안보임
                        deleteModalButtonDetail.style.display = 'none';
                        ModifyButton.style.display = 'none';
                    }
                });


                // 모달 창의 높이 계산
                var modalHeight = eventModalDetail.clientHeight;

                // 클릭한 위치를 기준으로 모달창 위치 설정
                var modalLeft = arg.jsEvent.clientX;
                var modalTop = arg.jsEvent.clientY;

                eventModalDetail.style.left = modalLeft + 'px';
                eventModalDetail.style.top = modalTop + 'px';
                eventModalDetail.style.display = 'block';

                // setTimeout을 사용하여 모달이 표시된 후에 하단설정
                setTimeout(function () {
                    var modalHeight = eventModalDetail.clientHeight;
                    var screenHeight = window.innerHeight;

                    if (modalTop + modalHeight > screenHeight) {
                        modalTop = screenHeight - modalHeight - 10; // 10은 여유 여백
                        eventModalDetail.style.top = modalTop + 'px';
                    }

                }, 0);
            },
        });
    }
//===================================상세 모달창 띄우기===================================


// 수정된 내용을 서버로 보내는 Ajax 요청
    function sendToServer(clickedEventId, updatedInfo) {
        $.ajax({
            type: 'PUT',
            url: '/api/schedule/' + clickedEventId + '/update',
            contentType: 'application/json',
            data: JSON.stringify(updatedInfo),
            success: function (updateResponse) {
                console.log('Server response (Update Schedule):', updateResponse);

                eventModalDetail.classList.add('show'); // 모달 창 나타내기

                // 콘솔 로그 출력 - 수정 후 데이터 확인
                console.log('After Update:', updatedInfo);

                // 서버로부터 업데이트된 전체 일정을 가져와 FullCalendar에 반영
                getAllEvents();

            },
            error: function (error) {
                console.error('Error updating schedule:', error.responseText);
                // 업데이트 실패 시 추가적인 작업 수행
            }
        });
    }

// db에 수정 내용 저장
//     function handleModifySave(event, arg) {
    function handleModifySave(clickedEventId) {

        // event를 사용하여 이벤트 객체 정보에 접근
        var updatedInfo = {
            title: ModifyTitle.value.trim(),
            content: ModifyContent.value.trim(),
            startDate: ModifyStartDate.valueAsDate,
            endDate: ModifyEndDate.valueAsDate,
            color: ModifyColor.value
        };

        // 콘솔 로그 출력 - 수정 전 데이터 확인
        // console.log('Before Update:', arg.event);

        // 수정된 내용을 서버로 보내는 Ajax 요청
        sendToServer(clickedEventId, updatedInfo);

        // 수정 모달 닫기
        ModifyModal.style.display = 'none';

    }

//===================================수정 모달창 띄우기===================================
    function showModifyModal(arg) {
        // 기존에 등록된 리스너 제거
        ModifySaveButton.removeEventListener('click', handleModifySave);

        eventModalDetail.style.display = 'none';    // 상세정보 닫기
        ModifyModal.style.display = 'block'; // 수정모달을 나타내는 로직

        // 이벤트 정보 유지
        var eventTitle = arg.event.title;
        var eventContent = arg.event.extendedProps.content; // 이벤트 내용을 가져오기
        var eventStart = arg.event.start;
        var eventEnd = arg.event.end;
        var eventColor = arg.event.backgroundColor;

        // 수정 모달 정보 표시
        ModifyModal.style.display = 'block';

        // 모달 창의 높이 계산
        var modalHeight = ModifyModal.clientHeight;

        // 클릭한 위치를 기준으로 모달창 위치 설정
        var modalLeft = arg.jsEvent.clientX;
        var modalTop = arg.jsEvent.clientY;

        // 모달창이 화면 하단을 넘어갈 경우 조정
        var screenHeight = window.innerHeight;

        if (modalTop + modalHeight > screenHeight) {
            modalTop = screenHeight - modalHeight;
        }

        ModifyModal.style.left = modalLeft + 'px';
        ModifyModal.style.top = modalTop + 'px';

        // 이벤트 정보를 수정 모달에 표시
        ModifyTitle.value = eventTitle;
        ModifyStartDate.valueAsDate = eventStart;

        // 시작일과 종료일이 같으면 종료일을 시작일의 값으로 설정
        if (eventEnd && eventStart.getTime() === eventEnd.getTime()) {
            ModifyEndDate.valueAsDate = eventStart;
        } else {
            ModifyEndDate.valueAsDate = eventEnd || eventStart; // 종료일이 없으면 시작일 값 사용
        }

        ModifyContent.value = eventContent;
        ModifyColor.value = eventColor;
    }


    // AJAX를 이용하여 서버로 데이터 전송
    function saveToServer() {
        var title = eventTitleInput.value.trim();
        var content = eventContentInput.value.trim();
        if (!title) return; // 타이틀이 비어있으면 아무 동작도 하지 않음

        // 클라이언트(날짜클릭을 기준으로 받은 데이터값)
        var startDateInputValue = startDateInput.valueAsDate;
        var endDateInputValue = endDateInput.valueAsDate;

        // 사용자가 선택한 색상 가져오기
        selectedColor = eventColorInput.value;

        $.ajax({
            type: 'POST',
            url: '/saveW1004', // 실제 백엔드 서버의 URL에 맞게 조정
            contentType: 'application/json',
            data: JSON.stringify({
                // memberId: memberId,
                id: eventId,
                title: title,
                content: content,
                startDate: startDateInputValue,
                endDate: endDateInputValue,
                color: selectedColor,
                allDay: true, // 이 부분 추가
                loggedInMemberId: getLoggedInMemberId(), // 현재 로그인한 멤버의 ID 추가
            }),
            success: function (response) {
                // 서버로부터의 응답 처리

                var event = {
                    id: eventId,
                    title: title,
                    content: content,
                    start: startDateInputValue,
                    end: endDateInputValue,
                    allDay: true, // 이 부분 추가
                    color: selectedColor
                };


                calendar.addEvent(event);
                // calendar.addEvent(eventDataTransform(event)); // FullCalendar에 이벤트 추가
                console.log('Event object:', event);


                // 서버로부터 업데이트된 전체 일정을 가져와 FullCalendar에 반영
                getAllEvents();

                console.log('Event saved successfully:', response);
                // 여기에 성공적으로 저장되었을 때의 로직을 추가

            },
            error: function (error) {
                // 서버로부터의 응답에 에러가 있을 때 처리
                console.error('Error saving event:', error.responseText);
                // 여기에 에러 발생 시의 로직을 추가
            }
        });
    }
});





document.addEventListener('DOMContentLoaded', function () {


});

// 문자열 변환
function formatDate(date) {
    // 날짜 객체가 유효한지 확인
    if (date instanceof Date && !isNaN(date)) {
        // 날짜를 "YYYY-MM-DD" 형식의 문자열로 변환
        var year = date.getFullYear();
        var month = String(date.getMonth() + 1).padStart(2, '0');
        var day = String(date.getDate()).padStart(2, '0');
        return year + '-' + month + '-' + day;
    } else {
        return ''; // 유효하지 않은 경우 빈 문자열 반환
    }
}