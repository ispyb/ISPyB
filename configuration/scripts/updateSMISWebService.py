import traceback
import sys
from suds.client import Client
from suds.transport.http import HttpAuthenticated
import os, shutil
from contextlib import closing

from datetime import datetime
from random import randint
import time

#import ConfigParser


################################
# SOAP CLIENT SUDS
################################    
class BiosaxsClient( ):
    def __init__(self, user, password, url):
        self.user = user
        self.password = password
        self.url = url

    def getClient(self):
        timeout = 20
        httpAuthenticatedToolsForAutoprocessingWebService = HttpAuthenticated( username = self.user, password = self.password )     
        return Client( self.url, transport = httpAuthenticatedToolsForAutoprocessingWebService, cache = None, timeout = timeout )
    
    
        
                            
if __name__ == "__main__":
    #config = ConfigParser.ConfigParser()
    #config.read('ispyb.properties')

    print '------------------------------'
    print 'Argument List:', str(sys.argv)


    ################################
    # CONNECTION PARAMETERS
    ################################
    user = *****
    passw = ****
    url = "http://***:8080/ispyb-ws/ispybWS/UpdateFromSMISWebService?wsdl"
    biosaxs = BiosaxsClient(user, passw, url)

    ################################
    # Updating proposal from SMIS
    ################################
    print "Updating..."
    macros = biosaxs.getClient().service.updateFromSMIS()
    print "Done"