package ci.dgmp.sigomap.authmodule.controller.services.spec;

import ci.dgmp.sigomap.authmodule.model.dtos.menu.CreateMenuDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.menu.PrvToMenuDTO;
import ci.dgmp.sigomap.authmodule.model.entities.Menu;

import java.net.UnknownHostException;

public interface IMenuMutatorService
{

    Menu createMenu(CreateMenuDTO dto) throws UnknownHostException;
    void addPrvToMenu(PrvToMenuDTO dto) throws UnknownHostException;
    void removePrvToMenu(PrvToMenuDTO dto) throws UnknownHostException;
}
