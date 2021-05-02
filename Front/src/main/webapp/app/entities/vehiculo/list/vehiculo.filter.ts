export class VehiculoFilter{
    constructor(
        public matricula?: string,
        public marca?: string
    ){}
       
    toMap():any{
        const map = new Map();

        /* Nombre */
        if(this.matricula != null && this.matricula !== ""){
            map.set('matricula.contains',this.matricula);
        } 

        /* Apellido */
        if(this.marca != null && this.marca !== ""){
            map.set('marca.contains',this.marca);
        } 

        return map;
    }
}