package angoothape.wallet.di.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "PidOptions", strict = false)
public class PidOptions {

    public PidOptions() {
    }

    @Attribute(name = "ver", required = false)
    public String ver;

    @Element(name = "Opts", required = false)
    public angoothape.wallet.di.model.Opts Opts;

    @Element(name = "CustOpts", required = false)
    public CustOpts CustOpts;

}
