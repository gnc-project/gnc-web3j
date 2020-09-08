package gnc.web3j;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

public class GetTx {

    static String txhash="0x4f3c3a458246fd4d3c205cb0787dd477bc7a9ac198824fbd3349486a4e5084e4";

    public static void main(String[] args) throws IOException {
        Web3j web3j = Web3j.build(new HttpService("http://chain-node.galaxynetwork.vip/"));

        EthTransaction ethTransaction =  web3j.ethGetTransactionByHash(txhash).send();
        System.out.println(ethTransaction.getTransaction().get().getValue());
    }
}
