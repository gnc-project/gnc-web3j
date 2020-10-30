package gnc.web3j;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;


public class Send {
    static String privateKeyStr = "803bceafaf0950bbd10c7a812718af444906d9a2ed53301feb71d8342e6ced2a";//

    public static void main(String[] args) throws IOException {

        Web3j web3j = Web3j.build(new HttpService("http://chain-node.galaxynetwork.vip/"));
        BigInteger chain = web3j.ethChainId().send().getChainId();
        System.out.println("chain----->"+chain);

        String fromAddress = Credentials.create(privateKeyStr).getAddress();
//        System.out.println("fromaddress--->"+fromAddress);
        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(fromAddress,
                        DefaultBlockParameterName.PENDING).send();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        System.out.println("fromAddress-->"+fromAddress);
        System.out.println("nonce--->"+nonce);

    }
}
