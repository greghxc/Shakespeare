# Shakespeare: Google Places Transportation Quotes

A simple akka-http service to create mileage-based transportation quotes
from Google Places data, as used in the town car industry.

## Setup and Configuration
A few bits of setup are required to configure Shakespeare, including an
API key and fare information.
### Google API Key
The Google Places API requires an API key. Shakespeare expects an
environment variable name "googleApiKey" containing your API key.
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


