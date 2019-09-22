package algo;

import org.junit.Test;

import java.util.Scanner;

import static algo.Abbreviation.abbreviation;
import static org.junit.Assert.*;

public class AbbreviationTest {

    @Test
    public void testAbbreviation() {
        assert abbreviation("daBcd", "ABC").equals("YES");
    }

    @Test
    public void testAbbreviation2() {
        assert abbreviation("KXzQ", "K").equals("NO");
    }

    @Test
    public void testAbbrevation3() {
        assert abbreviation("UMKFW", "UMKFW").equals("YES");
    }

    @Test
    public void testAbbrevation4() {
        String a = "SYIHDDSMREKXOKRFDQAOZJQXRIDWXPYINFZCEFYyxu";
        String b = "SYIHDDSMREKXOKRFDQAOZJQXRIDWXPYINFZCEFY";
        assert abbreviation(a, b).equals("YES");
    }

    @Test
    public void testWithInput() {
        Scanner scanner = TestInputScanner.getScanner("abbreviation.txt");
        int q = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
        for (int qItr = 0; qItr < q; qItr++) {
            String a = scanner.nextLine();
            String b = scanner.nextLine();
            String result = abbreviation(a, b);
            System.out.println(result);

        }

        scanner.close();
    }

    @Test
    public void testWithInput2() {
        Scanner scanner = TestInputScanner.getScanner("abbreviation2.txt");
        int q = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
        for (int qItr = 0; qItr < q; qItr++) {
            String a = scanner.nextLine();
            String b = scanner.nextLine();
            String result = abbreviation(a, b);
            System.out.println(result);

        }

        scanner.close();
    }

    @Test
    public void testAbbrevation5() {
        String a = "hHhAhhcahhacaccacccahhchhcHcahaahhchhhchaachcaCchhchcaccccchhhcaahhhhcaacchccCaahhaahachhacaahhaachhhaaaCalhhchaccaAahHcchcazhachhhaaahaahhaacchAahccacahahhcHhccahaachAchahacaahcahacaahcahacaHhccccaahaahacaachcchhahhacchahhhaahcacacachhahchcaAhhcaahchHhhaacHcacahaccccaaahacCHhChchhhahhchcahaaCccccahhcaachhhacaaahcaaaccccaacaaHachaahcchaahhchhhcahahahhcaachhchacahhahahahAahaAcchahaahcaaaaahhChacahcacachacahcchHcaahchhcahaachnachhhhcachchahhhacHhCcaHhhhcaCccccaaahcahacahchahcaachcchaachahhhhhhhhcahhacacCcchahccaaaaaHhhccaAaaaCchahhccaahhacaccchhcahhcahaahhgacahcahhchcaaAccchahhhaahhccaaHcchaccacahHahChachhcaaacAhacacaacacchhchchacchchcacchachacaahachccchhhaccahcacchaccaahaaaccccccaaaaaaaHhcahcchmcHchcchaaahaccchaaachchHahcaccaaccahcacacahAhaacaacaccaccaaacahhhcacAhaCchcaacCcccachhchchcchhchahchchahchchhchcacaachahhccacachaAhaaachchhchchchhaachahaahahachhaaaccacahhcacchhhaaachaaacAahhcachchachhhcacchacaaChCahhhccahChaachhcahacchanaaacchhhccacacchcahccchAcahacaaachhacchachccaaHacaacAhahcCh";
        String b = "HAHHCHAACCCAHCHHAHHAHCACCHCCHHCAAHHCACCCAHHHACAAHHHHCHHCAHHAHHAAAHAACAAHAHHCAHAHACHACHCHACACHAAHHAAAHCAHHACACAACHHHCHAHCAHCHHHAHAHACCAAAHCHHCHHCCAACCCCAACHACAACAAHACHCHAHHACCHCAHHHAAACHACAACHCACACAHHCCHAHACCCACCAACHCHHHCCCCCHCCAHHCAAHHAHHHHHHHAACCCCAHCCAAAAAHHHAAAACCAHHCAHACACCHHCHAHAHHCHAACHHHHHCCHCCAHAHCHCAAACCACCCCHACCACHHACHHACACHACCAACCCCAAAAHHAHCHHHCCAHCCHACHHAHCCACACCHAHAAACACCCCAHCCAHACCCCCCHCCHHCHHHHCHCHCAHHHACHAHAACCCAAAACHAACAAAHHAAHAAAHACHHCACHCCHCHAACHACACHHCCCCCAHCACHAAAHCHCAHACAAC";
        assert abbreviation(a, b).equals("YES");
    }

    @Test
    public void testAbbrevation6() {
        String a = "evivevvVvevvEeeevVevevVvevEVevEeeEVVveevEvevevEeEeEEeeVevvvVVvevveevevEeVeVvvveeeeeivVveEevvEeveevvevvvVeevVeeeeveebveeVvvvvvEVEvEvvvvEEVVeeeVvEVEvevvevEvveVeEeeeVveVEVvvvvEeeVVvVeEEVvEEeeeveveeevVVeeeevVveeVvevVVevevvEEveVEEVVeeeEeveevevEvvvvevveeeeVEvevvEVvvvVevevvvveVeevVveevVeEevEEeeevVeieeeVvvvevvVeevvveeeevvVevEeevvvevveveevvveeeveeevVivevvevVvVeeevvEveevEEVeeVVEeeeevvveeEeveevvVeeEeevvvveeVvveveeeEveeeEeveeveVeVEveevvvVeevvveeEevVveeeVEeevEveevvVeveeeeVVVVeVEvvEVveveEvVeeeeEeeevvVEvveevvEeevevvVEeeeEvvvevvVvEVEvvvvvVvevEvVvVeevVevvVvEvveeeeeeEeveeVvEvVVvveveEvvVveeevvvViEeEEveeevvVevEveVVVeEeevVeveeEeeeeeveEvvVEeeEeveEvvvvveeVveVeVveeeVveEveeVvEVeEEeveeeVvvveEeveveeeVevevvveveVEEeveveevevveeevVeeeveveeveeveevvvEeeEvVeveevVEVEvvVVeeveVevViEEvVevevvVVEVVvvVeiEvVeevevvvEevvEvvvvevVveeVvvEevEeEEvEeeeeevveveevveveeeeVVeeveevvvveeVEEEveeeveeeEveeVVeeeVvEvvevveevvveVveeievivvVvvevevveeEeVEEeveveeVEEveviEveeivVvvVIEeEEvEveevEEveVvvEEveVeVEVvveveeVvVeEveeVVvveveeveVeveevvevEeVeeveVeEeVve";
        String b = "VEVVEEVEVVEVEEVVVEVEEEEEEEVVVEVEVEVVEEVEEEVVVEEVEEVVVEVEEVEEVVVVEVEVEVVEEVEEEEVEVVEEEVVVEEEVEEEVVEEVVVVVVEVVVVVEEVEEVVEEEVEVVVVEVEVEVEVVVEVVEEVVVEEEEEEVVIEEVVVVEEVVVEVEVVVEVVVEVIVEVVVVEEEEEVVVEVEEEVEVEEVEVVVEVVVEEEEEEVEVEVEVVEVVVVVEVEVEVEEEEVVEVEEEEVVEEEEEVEVEVEVVVEEVVEVVVVEEEEEVEVVVVVEEEVEEVEEEEVEEVVVEEEVEEVEEEEEEEVVVEVVEEVEVEEEVVVVEVEEVEVEVVVEEVEVVEVVVEVEEEEVVEVEVVEEVVVEEVVVVEVVVVEVVVVVEVEVVVVEVEEEEEEVVVEVVEEVVVEEVEEEEEEEEVVEEVEVEVVEVEIVVEEVEEVVEEVEEEIVVVVIEEEEEVEEEVEEEVVEVVEVVEVVEVEVEVEVVVEVE";
        assert abbreviation(a, b).equals("NO");
    }



    @Test
    public void testWithInput3() {
        Scanner scanner = TestInputScanner.getScanner("abbreviation3.txt");
        int q = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
        for (int qItr = 0; qItr < q; qItr++) {
            String a = scanner.nextLine();
            String b = scanner.nextLine();
            String result = abbreviation(a, b);
            System.out.println(result);

        }
        scanner.close();
    }

}