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

public class Main {
    static String privateKeyStr = "a59bc058eb76eea5b64f1e55a803aa0968efda8a943f8f7eb835a6df9ac3a835";
    static String toAddress = "GNC1fa42f91e2db08d2e66d18a969bf3d95e855c9db";
    static String outBalance = "500";//ether
    static int chainid = 37021;

    public static void main(String[] args) throws IOException {

        Web3j web3j = Web3j.build(new HttpService("http://chain-node.galaxynetwork.vip/"));
        BigInteger chain = web3j.ethChainId().send().getChainId();
        System.out.println("chain----->"+chain);

        String fromAddress = Credentials.create(privateKeyStr).getAddress();

        BigInteger b=web3j.ethGetBalance(fromAddress,DefaultBlockParameterName.LATEST).send().getBalance();
        System.out.println("balance--->"+b);
        BigDecimal bigDecimal = Convert.fromWei(new BigDecimal(b),Convert.Unit.ETHER);
        System.out.println("---->"+bigDecimal);

        EthGetTransactionCount ethGetTransactionCount =
                web3j.ethGetTransactionCount(fromAddress,
                        DefaultBlockParameterName.PENDING).send();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        byte[] privateKey =
                Hex.decode(privateKeyStr.replace("GNC", ""));

        Credentials keys = Credentials.create(ECKeyPair.create(privateKey));

        BigInteger weiBalance =Convert.toWei(outBalance, Convert.Unit.ETHER).toBigInteger();

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
