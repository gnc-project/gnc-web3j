package gnc.web3j;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

//
//0x803bceafaf0950bbd10c7a812718af444906d9a2ed53301feb71d8342e6ced2a
//
//        GNC4b78945AF4666390fb34592c04992e51A102bF24
//        GNC0003F97ab92AA9D8Ee92bBBd990f93380E752991
//        GNC86056D210eA7Bc23337aCaBE96dE275E584a67ce

//0x9c7930ee33cf5f14ec58f7aa73f465423e02ab77  803bceafaf0950bbd10c7a812718af444906d9a2ed53301feb71d8342e6ced2a
//0x2849d55cf6fa1ef2104f99bd5d14792b068b77ca 659b4085690812e650a6065ce6e01e2e84b7b753a64697db977c7f231d5ddc97
public class Main {
    static String privateKeyStr = "803bceafaf0950bbd10c7a812718af444906d9a2ed53301feb71d8342e6ced2a";//
    static String toAddress = "GNC69310bc3618fa049db1824a06f262146bc62ee23";
    static String outBalance = "30000";//ether
    static int chainid = 37021;

    public static void main(String[] args) throws IOException {

        Web3j web3j = Web3j.build(new HttpService("http://chain-node.galaxynetwork.vip/"));
        BigInteger chain = web3j.ethChainId().send().getChainId();
        System.out.println("chain----->"+chain);

        String fromAddress = Credentials.create(privateKeyStr).getAddress();

        System.out.println("fromAddress-->"+fromAddress);

        BigInteger b=web3j.ethGetBalance(fromAddress,DefaultBlockParameterName.LATEST).send().getBalance();
        System.out.println("balance--->"+b);
        BigDecimal bigDecimal = Convert.fromWei(new BigDecimal(b),Convert.Unit.ETHER);
        System.out.println("---->"+bigDecimal);

        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(fromAddress,
                        DefaultBlockParameterName.PENDING).send();

//        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        BigInteger nonce = new BigInteger("7");

        byte[] privateKey =
                Hex.decode(privateKeyStr.replace("GNC", ""));

        Credentials keys = Credentials.create(ECKeyPair.create(privateKey));

//        BigInteger weiBalance =Convert.toWei(outBalance, Convert.Unit.ETHER).toBigInteger();
        BigInteger weiBalance = new BigInteger("700000000000000000");

        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = new BigInteger("21000");

        RmRawTransaction etherTransaction = RmRawTransaction.createTransaction(nonce,
                gasPrice,
                gasLimit,
                toAddress, weiBalance, "");

        byte[] bytes = RmTransactionEncoder.signMessage(etherTransaction, chainid, keys, true);
        String hexValue = Numeric.toHexString(bytes);

        EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).send();


        boolean hasError = ethSendTransaction.hasError();
        if (hasError) {
            System.out.println(ethSendTransaction.getError().getMessage());
            System.out.println(hasError);
            return;
        }

        String transactionHash = ethSendTransaction.getTransactionHash();
        System.out.println("txHash---> "+transactionHash);
    }


    public static void create_address() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();

        String sPrivatekeyInHex = privateKeyInDec.toString(16);

        WalletFile aWallet = Wallet.createLight("fdsfaadsfasd", ecKeyPair);
        String sAddress = aWallet.getAddress();


        System.out.println("address"+ "0x" + sAddress);
        System.out.println("privatekey "+sPrivatekeyInHex);

    }
}
