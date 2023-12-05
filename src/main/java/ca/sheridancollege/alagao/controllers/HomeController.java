package ca.sheridancollege.alagao.controllers;

import ca.sheridancollege.alagao.database.DatabaseAccess;
import ca.sheridancollege.alagao.database.TicketDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

    @Autowired
    private DatabaseAccess database;
    @Autowired
    private TicketDatabase tDa;

}
