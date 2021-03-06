/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package World;

/**
 *
 * @author Bailey
 */
public enum Tiles {
    WATER_DEEP(new String[]{"water.png"}, EnumMaterial.WATER),
    CUSTOM(new String[]{"custom"}, EnumMaterial.CUSTOM);
    
    protected String[] ids;
    protected EnumMaterial material;
    
    Tiles(String[] ids, EnumMaterial material){
        this.ids = ids;
        this.material = material;
    }
    
    public String getTexture(){
        return this.ids[(int)(Math.floor(this.ids.length)*Math.random())];
    }
}
