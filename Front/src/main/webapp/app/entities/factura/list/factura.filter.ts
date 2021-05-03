export class FacturaFilter{
    constructor(
        
        public estado?:string
       
    ){}
       
    toMap():any{
        const map = new Map();

       

        if(this.estado != null && this.estado !== ""){
            map.set('estado.equals',this.estado);
        }
        
        return map;
    }
}