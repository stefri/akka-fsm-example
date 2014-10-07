import akka.actor.{Actor, FSM}

// received actions
sealed trait Action
case object PlayPause extends Action
case object Stop extends Action
case object SkipForward extends Action
case object SkipBackward extends Action
case object NextTrack extends Action

// states
sealed trait State
case object Idle extends State
case object Playing extends State
case object Pausing extends State

// data
sealed trait Data
case object Uninitialized extends Data
case class Track(number: Int) extends Data


class Player extends Actor with FSM[State, Data] {

  startWith(Idle, Uninitialized)

  when(Idle) {
    case Event(PlayPause, Uninitialized) =>
        goto(Playing) using Track(1)
  }

  when(Playing) {
    case Event(PlayPause, _) =>
        goto(Pausing)
    case Event(SkipForward | NextTrack, t: Track) =>
        log.info(s"play track number ${t.number + 1}")
        stay using Track(t.number + 1)
    case Event(SkipBackward, t: Track) if t.number > 1 =>
        log.info(s"play track number ${t.number - 1}")
        stay using Track(t.number - 1)
    case Event(SkipBackward, t: Track) if t.number == 1 =>
        stay
    case Event(Stop, _) =>
      goto(Idle) using Uninitialized
  }

  when(Pausing) {
    case Event(PlayPause, _) =>
      goto(Playing)
    case Event(Stop, _) =>
      goto(Idle) using Uninitialized
  }

  onTransition {
    case Idle -> Playing => log.info("playing ...")
    case Pausing -> Playing => log.info("playing again ...")
  }

  initialize()
}

