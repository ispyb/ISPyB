<%--------------------------------------------------------------------------------------------------
This file is part of ISPyB.

ISPyB is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ISPyB is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.

Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
--------------------------------------------------------------------------------------------------%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="ispyb.common.util.Constants"%>

<DIV align=center>
		<P class=footer>
          <A href="http://www.esrf.fr/" target=_blank>ESRF</A> | 
          <A href="http://www.embl-grenoble.fr/" target=_blank>EMBL</A> | 
          <A href="http://www.bm14.ac.uk/" target=_blank>BM14</A> | 
          <A href="http://www.spineurope.org/" target=_blank>SPINE</A> | 
          <A href="http://www.ebi.ac.uk/pdbe/" target=_blank>MSD</A> | 
          <a href="http://www.maxiv.se/">MAX IV</a> |
          <A href="http://www.cells.es/">ALBA</A> |
          <c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
           	| <A href="http://www.diamond.ac.uk/" target=_blank>DLS</A> 
          </c:if>
          <a href="http://www.synchrotron-soleil.fr" target="_blank">SOLEIL</a>
      </P>
   	<c:if test="${SITE_ATTRIBUTE eq 'ESRF'}">
      <P class=small>
      		Copyright � 2004 ISPyB All rights reserved. <br/>
      </P>
    </c:if>
    <c:if test="${SITE_ATTRIBUTE eq 'DLS'}">
      <P class=small>
      		Copyright � 2008 ISPyB All rights reserved. <br/>
      </P>
    </c:if>
    <c:if test="${SITE_ATTRIBUTE eq 'EMBL' or SITE_ATTRIBUTE eq 'ALBA'}">
      <P class=small>
      		Copyright � 2014 ISPyB All rights reserved. <br/>
      </P>
    </c:if>    
    <c:if test="${SITE_ATTRIBUTE eq 'MAXIV'}">
      <P class=small>
      		Copyright � 2014 ISPyB All rights reserved. <br/>
      </P>
      	<!-- Piwik -->
		<script type="text/javascript">
		  var _paq = _paq || [];
		  _paq.push(['trackPageView']);
		  _paq.push(['enableLinkTracking']);
		  (function() {
		    var u="https://piwik.maxiv.lu.se/piwik/";
		    _paq.push(['setTrackerUrl', u+'piwik.php']);
		    _paq.push(['setSiteId', 2]);
		    var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
		    g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
		  })();
		</script>
		<noscript><p><img src=https://piwik.maxiv.lu.se/piwik/piwik.php?idsite=2" style="border:0;" alt="" /></p></noscript>
		<!-- End Piwik Code -->
    </c:if>    
    <c:if test="${SITE_ATTRIBUTE eq 'SOLEIL'}">
      <P class=small>
      		Copyright � 2014 ISPyB All rights reserved. <br/>
      </P>
    </c:if>
</DIV>
