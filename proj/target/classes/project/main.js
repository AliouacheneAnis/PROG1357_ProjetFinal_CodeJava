var faunadb = window.faunadb
var q = faunadb.query
var Levelwater, Quality;
var client = new faunadb.Client({
  secret: 'fnAE_mGgjoACTTxwwwhPkM7akuitNGHl-aZHIRMV',
  // Adjust the endpoint if you are using Region Groups
  endpoint: 'https://db.fauna.com/',
})

client.query(
    
    q.Paginate(
      q.Match(q.Index("data_sort")),
      {
        size: 1
      }
    )
    )
    .then(function (res) { 
    
    const tableBody = document.getElementById('data-table');
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${res.data[0][1]}</td>
        <td>${res.data[0][2]}</td>
      `;
      tableBody.appendChild(row);
      console.log('Result:', res.data[0][2]) ;})
    .catch(function (err) { console.log('Error:', err) })

// Get the data from FaunaDB
async function getData() {
  const response = await client.query(
    
    q.Paginate(
      q.Match(q.Index("data_sort_by_ts"))
    )
    );


console.log(response.data);
const t = [];
const waterLevelData = []; 
const waterQualityData = [];
  // Extract the water level and quality data
  for (let index = 0; index < response.data.length; index++) {
    t[index] = new Date(response.data[index][0]);
    waterLevelData[index] = response.data[index][1]; 
    waterQualityData[index] = response.data[index][2]
    
  }
  console.log(t); 
  console.log(waterQualityData);
  console.log(waterLevelData);

  const ctx1 = document.getElementById('chart1').getContext('2d');
const chart1 = new Chart(ctx1, {
    type: 'line',
    data: {
        labels: waterQualityData,
        datasets: [{
            label: 'Water Level',
            data: waterLevelData,
            backgroundColor: 'rgba(0, 0, 0, 0)', // Set the background color of the chart to transparent
            borderColor: 'blue', // Set the border color of the chart to blue
            borderWidth: 1 // Set the border width of the chart to 1
          }]
        },
        options: {
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: true // Set the minimum value of the y-axis to zero
              }
            }]
          }
        }
      });
}

// Call getData() once initially to display the initial data and charts
getData();

setInterval(function() {
  location.reload();
}, 6000);