package endertitan.echolib.resourcenetworks.graph

interface IVertex {
    var marked: Boolean
    var adjacent: HashSet<IVertex>
}