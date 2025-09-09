# Assignment 1
  This stack takes in a database of trips (city, minutes, fare) defined in 'db/init.sql'. Then, it runs the python application that is located in 'app/main.py'. This application currently displays the number of trips in the database, the average fare by city, and the top trips by minutes.


  To run: {docker compose up --build} or {make up}


  Output (located in out/summary.json):
```
{
  "total_trips": 6,
  "avg_fare_by_city": [
    {
      "city": "Charlotte",
      "avg_fare": 16.25
    },
    {
      "city": "New York",
      "avg_fare": 19.0
    },
    {
      "city": "San Francisco",
      "avg_fare": 20.25
    }
  ],
  "top_by_minutes": [
    {
      "city": "San Francisco",
      "minutes": 28,
      "fare": 29.3
    },
    {
      "city": "New York",
      "minutes": 26,
      "fare": 27.1
    },
    {
      "city": "Charlotte",
      "minutes": 21,
      "fare": 20.0
    },
    {
      "city": "Charlotte",
      "minutes": 12,
      "fare": 12.5
    },
    {
      "city": "San Francisco",
      "minutes": 11,
      "fare": 11.2
    },
    {
      "city": "New York",
      "minutes": 9,
      "fare": 10.9
    }
  ]
}
```
