export class VehiculoFilter{
    constructor(
        public matricula?: string,
        public estado?: string
    ){}
       
    toMap():any{
        const map = new Map();

        /* Nombre */
        if(this.matricula != null && this.matricula !== ""){
            map.set('matricula.contains',this.matricula);
        } 

        /* Apellido */
        if(this.estado != null && this.estado !== ""){
            map.set('estado.equals',this.estado);
        } 

        return map;
    }
}