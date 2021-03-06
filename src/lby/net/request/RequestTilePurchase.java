
package lby.net.request;

// Java Imports
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import lby.core.world.Zone;

// Other Imports
import shared.core.GameServer;
//DAOS used 
import shared.db.PlayerDAO;
import lby.db.world.WorldZoneDAO;

import shared.model.Player;
import shared.model.Account;
import lby.net.response.ResponseLogin;
import shared.util.DataReader;
import shared.util.Log;

//response for price
import lby.net.response.ResponseTilePrice;
import lby.net.response.ResponseTilePurchase;
import shared.model.Ecosystem;
/**
 *
 * @author Paul Broestl
 */
public class RequestTilePurchase extends GameRequest{
    
    //hardcoded 
    private int world_id = 1;
    private int zone_id;
    private int zone_capacity;
    private int price;
    
    private List<Zone> playerZones = null;
    
        @Override
    public void parse(DataInputStream dataInput) throws IOException {
        
        zone_id = DataReader.readInt(dataInput);
        
    }
        @Override
    public void process() throws Exception {

        ResponseTilePurchase response = new ResponseTilePurchase();
        Ecosystem ecosystem = client.getPlayer().getEcosystem();
         Log.println("eco: " +ecosystem);
        if(ecosystem == null)
        {
            short type = 1;
            ecosystem = new Ecosystem(client.getPlayer().getAccountID(),client.getPlayer().getAccountID(),client.getPlayer().getID(),client.getPlayer().getName(),type);
            client.getPlayer().setEcosystem(ecosystem);
     
        }
        
        //check if the player owns any tiles
        playerZones = WorldZoneDAO.getZoneList(world_id,client.getPlayer().getID());
        if(playerZones.isEmpty())
        {
            Log.println("The Player owns no tiles, get a Freebie!");
            price = 0 ; 
            
        } else {
          
        // first calculate the price of the tile 
        Log.println("Carrying capacity " + WorldZoneDAO.getCarryingCapacity(zone_id));
        zone_capacity = (int)WorldZoneDAO.getCarryingCapacity(zone_id);
        //price is 10x the capacity 
        price = 10* zone_capacity;
        
        }
        System.out.println("Player Id: " +client.getPlayer().getID());
        Log.println("Player's current credits" + client.getPlayer().getCredits());

        //Deduct the price from the player credits
        System.out.println("Player Credits before: " + PlayerDAO.getCredits(client.getPlayer().getID()));
        PlayerDAO.changeCredits(client.getPlayer().getID(), -price);

        System.out.println("Player Credits After: " + PlayerDAO.getCredits(client.getPlayer().getID()));
        // update the zone database
        WorldZoneDAO.updateOwner(client.getPlayer().getID(),zone_id);
        
        int newCredits = PlayerDAO.getCredits(client.getPlayer().getID());
        //setting all the information 
        response.setPrice(price);
        response.setCredits(newCredits);
        response.setZoneId(zone_id);
        
        response.setStatus(ResponseTilePrice.SUCCESS);
        
        client.add(response);
        
    }
    
}
