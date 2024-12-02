$(document).ready(function () {
    // 도시 이름 매핑
    const cityMapping = {
        "서울": "Seoul",
        "부산": "Busan",
        "대구": "Daegu",
        "광주": "Gwangju",
        "인천": "Incheon",
        "대전": "Daejeon",
        "울산": "Ulsan",
        "제주": "Jeju",
        "경기도": "Gyeonggi-do",
        "강원도": "Gangwon-do",
        "수원": "Suwon",
        "고양": "Goyang",
        "용인": "Yongin",
        "창원": "Changwon",
        "성남": "Seongnam",
        "청주": "Cheongju",
        "천안": "Cheonan",
        "전주": "Jeonju",
        "안산": "Ansan",
        "남양주": "Namyangju",
        "안양": "Anyang",
        "김해": "Gimhae",
        "평택": "Pyeongtaek",
        "포항": "Pohang",
        "김포": "Gimpo",
        "의정부": "Uiwangbu",
        "구미": "Gumi",
        "광명": "Gwangmyeong",
        "양산": "Yangsan",
        "원주": "Wonju",
        "울진": "Uljin",
        "춘천": "Chuncheon"
    };

    function formatDate() {
        const today = new Date();
        const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' };
        return today.toLocaleDateString('ko-KR', options);
    }

    // 대전 날씨 정보 자동 로드
    function loadWeather(cityName = "대전") {
        $.ajax({
            url: `/weather?cityName=${cityName}`,
            method: 'GET',
            success: function (response) {
                console.log("날씨 응답:", response);
                if (response && response.status === "success") {
                    const date = formatDate();
                    $('#weather-title').html(`${cityName}의 현재 날씨 (${date})`);
                    $('#weather-content').html(response.data); // JSON 형태
                } else {
                    $('#weather-content').html(response.message || '날씨 정보를 불러오는 데 실패했습니다.');
                }
            },
            error: function (xhr, status, error) {
                console.error("날씨 요청 오류:", error);
                $('#weather-content').html('날씨 정보를 불러오는 데 실패했습니다.');
            }
        });
    }

    // 페이지 로드 시 대전 날씨 정보 불러오기
    loadWeather();

    // 검색 버튼 클릭 이벤트
    $('#search-button').on('click', function () {
        const selectedCity = $('#city-select').val();
        if (!selectedCity) {
            alert('지역을 선택하세요.');
            return;
        }

        // 선택한 지역의 날씨 정보 가져오기
        loadWeather(selectedCity);

        // 관광지 정보 가져오기
        $.ajax({
            url: `/tourism/search?cityName=${selectedCity}`,
            method: 'GET',
            success: function (data) {
                console.log("관광지 응답 데이터:", data);
                const list = data.items || [];
                if (list.length === 0) {
                    $('#tourism-list').html('<li>관광지 정보를 찾을 수 없습니다.</li>');
                    return;
                }

                // 관광지 목록 생성
                const html = list.map(item => {
                    return `
                        <li>
                            <strong>${item.title || '이름 없음'}</strong><br>
                            주소: ${item.address || '정보 없음'}<br>
                        </li>
                    `;
                }).join('');

                $('#tourism-list').html(html);
            },
            error: function (xhr, status, error) {
                console.error("관광지 요청 오류:", error);
                $('#tourism-list').html('<li>관광지 정보를 불러오는 데 실패했습니다.</li>');
            }
        });
    });
});