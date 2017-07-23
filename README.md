# Shakespeare: Google Places Transportation Quotes
![Build Status](https://circleci.com/gh/greghxc/shakespeare.svg?style=shield&circle-token=:circle-token)

A simple akka-http service to create mileage-based transportation quotes
from Google Places data, as used in the town car industry.

Shakespeare was created to replace a handful of disparate scripts used
in various projects in a framework I was interested in working in.

## Setup and Configuration
A few bits of setup are required to configure Shakespeare, including an
API key and fare information.
### Google API Key
The Google Places API requires an API key. Shakespeare expects an
environment variable name "GOOGLE_API_KEY" containing your API key.

For more info, see: [Get a key for Google Places API Web Service](https://developers.google.com/places/web-service/get-api-key)
### Quote Profiles
A __quote profile__ represents is the top level profile uses for calculating
fares. A quote profile will likely reference a class of vehicle, like
"sedan" or "SUV", that can provide services and will always have the
same maximum capacity.

A quote profile also contains multiple __fare profiles__. A fare profile
represents a class of reservation that may have different fare values,
for example airport reservations may have a higher minimum than in-town
point-to-point trips, regardless of mileage. This could be also used to
provide special fares for discount events or high-priority holidays.

Currently quote and fare profiles are set up in
`src/main/resources/fares.conf`. A sample template is provided. Quote
and fare profiles can be verified uses the __profiles__ endpoint.
### Fuel Surcharge and Service Fee
A __fuel surcharge__ is meant to be applied as a percentage of base +
(mileage * per mile), regardless of whether or not minimum was reached.

A __service__ fee is meant to be applied as a percentage of total quote
after all other calculations.

Both of these values are set in `src/main/resources/fares.conf` as a
decimal representation of the percentage (0.01 = 1%). Fuel surcharge and
service fee values can be verified uses the __profiles__ endpoint.

## Quick Start
    export GOOGLE_API_KEY=[YOUR_API_KEY]
    git clone https://github.com/greghxc/shakespeare.git
    cd shakespeare
    sbt run

Visit API docs at http://localhost:9000/swagger

## Contributions
I've stared recording todo's and bugs in
[Issues](https://github.com/greghxc/shakespeare/issues). Pull requests
and new Issues are welcome.