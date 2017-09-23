package models

import com.google.maps.model.{Distance, Duration}

case class DistanceMatrixResult (
  forwardDistance: Distance,
  reverseDistance: Distance,
  duration: Duration
)
