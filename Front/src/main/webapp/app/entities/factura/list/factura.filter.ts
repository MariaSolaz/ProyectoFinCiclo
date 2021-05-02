export class FacturaFilter{
    constructor(
        public vehiculo?: number,
       
    ){}
       
    toMap():any{
        const map = new Map();

  
        if(this.vehiculo != null && this.vehiculo !== 0){
            map.set('vehiculo.contains',this.vehiculo);
        } 

       
        return map;
    }
}