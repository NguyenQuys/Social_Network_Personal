/*<![CDATA[*/


var visitLabels = sortedDates.map(date => new Date(date).toLocaleDateString());
var visitData = sortedCounts;

var postLabels = postDates.map(date => new Date(date).toLocaleDateString());
var postData = postCounts;

// var colors = [
//     'rgba(255, 99, 132, 0.2)',
//     'rgba(54, 162, 235, 0.2)',
//     'rgba(255, 206, 86, 0.2)',
//     'rgba(75, 192, 192, 0.2)',
//     'rgba(153, 102, 255, 0.2)',
//     'rgba(255, 159, 64, 0.2)',
//     'rgba(199, 199, 199, 0.2)'
// ];

// var borderColors = [
//     'rgba(255, 99, 132, 1)',
//     'rgba(54, 162, 235, 1)',
//     'rgba(255, 206, 86, 1)',
//     'rgba(75, 192, 192, 1)',
//     'rgba(153, 102, 255, 1)',
//     'rgba(255, 159, 64, 1)',
//     'rgba(199, 199, 199, 1)'
// ];

var ctx1 = document.getElementById('NumberUserPerDay').getContext('2d');
var myLineChart = new Chart(ctx1, {
    type: 'line',
    data: {
        labels: visitLabels,
        datasets: [{
            // label: 'Số lượng truy cập theo ngày',
            data: visitData,
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 2
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        },
        plugins: {
            legend: {
                display: false // Ẩn legend (nhãn) của biểu đồ
            }
        }
    }
});

var ctx2 = document.getElementById('NumberPostsPerDay').getContext('2d');
var myBarChart = new Chart(ctx2, {
    type: 'bar',
    data: {
        labels: postLabels,
        datasets: [{
            // label: 'Số bài post theo ngày',
            data: postData,
            // backgroundColor: colors.slice(0, postData.length),
            // borderColor: borderColors.slice(0, postData.length),
            backgroundColor: 'rgba(240, 128, 128, 0.7)',
            borderColor: 'coral',
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        },
        plugins: {
            legend: {
                display: false // Ẩn legend (nhãn) của biểu đồ
            }
        }
    }
});
/*]]>*/