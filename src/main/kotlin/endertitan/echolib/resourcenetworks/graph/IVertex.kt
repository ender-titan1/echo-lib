package endertitan.echolib.resourcenetworks.graph

interface IVertex {
    var dirty: Boolean
    var marked: Boolean
    var adjacent: HashSet<IVertex>
}