export class ClienteFilter{
    constructor(
        public nombre?: string,
        public apellido?: string
    ){}
       
    toMap():any{
        const map = new Map();

        /* Nombre */
        if(this.nombre != null && this.nombre !== ""){
            map.set('nombre.contains',this.nombre);
        } 

        /* Apellido */
        if(this.apellido != null && this.apellido !== ""){
            map.set('apellido.contains',this.apellido);
        } 

        return map;
    }
}