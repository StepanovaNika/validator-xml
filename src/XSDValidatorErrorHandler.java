import org.xml.sax.ErrorHandler;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.List;

public class XSDValidatorErrorHandler implements ErrorHandler{

        private List<String> exceptions;

        public XSDValidatorErrorHandler(List<String> exceptions) {
            this.exceptions = exceptions;
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            String e = exception.getMessage() + ";" + exception.getLineNumber() + ";" + exception.getColumnNumber();
            exceptions.add(e);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            String e = exception.getMessage() + ";" + exception.getLineNumber() + ";" + exception.getColumnNumber();
            exceptions.add(e);
        }

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            String e = exception.getMessage() + ";" + exception.getLineNumber() + ";" + exception.getColumnNumber();
            exceptions.add(e);
        }

    }
