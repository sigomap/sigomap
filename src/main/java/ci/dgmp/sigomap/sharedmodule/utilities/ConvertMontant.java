package ci.dgmp.sigomap.sharedmodule.utilities;


import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class ConvertMontant {

    private static String Unite(long nombre) {
        String unite = "";
        switch (String.valueOf(nombre)) {
            case "0":
                unite = "zéro";
                break;
            case "1":
                unite = "un";
                break;
            case "2":
                unite = "deux";
                break;
            case "3":
                unite = "trois";
                break;
            case "4":
                unite = "quatre";
                break;
            case "5":
                unite = "cinq";
                break;
            case "6":
                unite = "six";
                break;
            case "7":
                unite = "sept";
                break;
            case "8":
                unite = "huit";
                break;
            case "9":
                unite = "neuf";
                break;
        }// fin switch
        return unite;
    }// -----------------------------------------------------------------------

    private static String Dizaine(long l) {
        String dizaine = "";
        switch (String.valueOf(l)) {
            case "10":
                dizaine = "dix";
                break;
            case "11":
                dizaine = "onze";
                break;
            case "12":
                dizaine = "douze";
                break;
            case "13":
                dizaine = "treize";
                break;
            case "14":
                dizaine = "quatorze";
                break;
            case "15":
                dizaine = "quinze";
                break;
            case "16":
                dizaine = "seize";
                break;
            case "17":
                dizaine = "dix-sept";
                break;
            case "18":
                dizaine = "dix-huit";
                break;
            case "19":
                dizaine = "dix-neuf";
                break;
            case "20":
                dizaine = "vingt";
                break;
            case "30":
                dizaine = "trente";
                break;
            case "40":
                dizaine = "quarante";
                break;
            case "50":
                dizaine = "cinquante";
                break;
            case "60":
                dizaine = "soixante";
                break;
            case "70":
                dizaine = "soixante-dix";
                break;
            case "80":
                dizaine = "quatre-vingt";
                break;
            case "90":
                dizaine = "quatre-vingt-dix";
                break;
        }// fin switch
        return dizaine;
    }// -----------------------------------------------------------------------

    private static boolean isNaN(String item) {
        try {
            Long.parseLong(item);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static String numberToLetter(double nombre)
    {
        return numberToLetter(new BigDecimal(nombre));
    }

    public static String numberToLetter(BigDecimal nombre)
    {
        long partieEntiere = (long) nombre.doubleValue();
        String partieEntiereLettre = numberToLetter(partieEntiere);
        String decimalString = nombre.subtract(new BigDecimal(partieEntiere)).toString();
        int decimalIndex = decimalString.indexOf(".");
        if(decimalIndex<0 || decimalString.equals("0")) return partieEntiereLettre;
        long partieDecimal = Integer.parseInt(decimalString.substring(decimalIndex + 1));
        String partieDecimaleLettre = numberToLetter(partieDecimal);
        String nombreLettre = partieEntiereLettre + " virgule " + partieDecimaleLettre;
        return nombreLettre;
    }

    public static String numberToLetter(long nombre) {
        int n;
        long reste;
        long quotient;
        long nb;
        String numberToLetter = "", sep = "-";
        // __________________________________
        if (String.valueOf(nombre).replace(" ", "").length() > 15)
            return "Dépassement de capacité";
        if (isNaN(String.valueOf(nombre).replace(" ", "")))
            return "Nombre non valide";
        nb = Long.valueOf(String.valueOf(nombre).replace(" ", ""));
        if (Math.ceil(nb) != nb)
            return "Nombre avec virgule non géré.";
        n = String.valueOf(nb).length();
        switch (n) {
            case 1:
                numberToLetter = Unite(nb);
                break;
            case 2:
                if (nb > 19) {
                    quotient = (long) Math.floor(nb / 10);
                    reste = nb % 10;
                    if (nb < 71 || nb > 79 && nb < 91) {
                        if (reste == 0)
                            numberToLetter = Dizaine(quotient * 10);
                        if (reste == 1) {
                            sep = quotient == 8 ? "-" : "-et-";
                            numberToLetter = Dizaine(quotient * 10) + sep
                                    + Unite(reste);
                        }
                        if (reste > 1)
                            numberToLetter = Dizaine(quotient * 10) + "-" + Unite(reste);
                    } else {
                        if (nb == 71)
                            sep = "-et-";
                        numberToLetter = Dizaine((quotient - 1) * 10) + sep + Dizaine(10 + reste);
                    }
                } else
                    numberToLetter = Dizaine(nb);
                break;
            case 3:
                quotient = (long) Math.floor(nb / 100);
                reste = nb % 100;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "cent";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "cent" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = Unite(quotient) + " cents";
                if (quotient > 1 && reste != 0)
                    numberToLetter = Unite(quotient) + " cent " + numberToLetter(reste);
                break;
            case 4:
                quotient = (long) Math.floor(nb / 1000);
                reste = nb - quotient * 1000;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "mille";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "mille" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " mille";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " mille " + numberToLetter(reste);
                break;
            case 5:
                quotient = (long) Math.floor(nb / 1000);
                reste = nb - quotient * 1000;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "mille";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "mille" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " mille";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " mille " + numberToLetter(reste);
                break;
            case 6:
                quotient = (long) Math.floor(nb / 1000);
                reste = nb - quotient * 1000;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "mille";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "mille" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " mille";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " mille " + numberToLetter(reste);
                break;
            case 7:
                quotient = (long) Math.floor(nb / 1000000);
                reste = nb % 1000000;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "un million";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "un million" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " millions";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " millions " + numberToLetter(reste);
                break;
            case 8:
                quotient = (long) Math.floor(nb / 1000000);
                reste = nb % 1000000;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "un million";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "un million" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " millions";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " millions " + numberToLetter(reste);
                break;
            case 9:
                quotient = (long) Math.floor(nb / 1000000);
                reste = nb % 1000000;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "un million";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "un million" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " millions";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " millions " + numberToLetter(reste);
                break;
            case 10:
                quotient = (long) Math.floor(nb / 1000000000);
                reste = nb - quotient * 1000000000;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "un milliard";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "un milliard" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " milliards";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " milliards " + numberToLetter(reste);
                break;
            case 11:
                quotient = (long) Math.floor(nb / 1000000000);
                reste = nb - quotient * 1000000000;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "un milliard";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "un milliard" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " milliards";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " milliards " + numberToLetter(reste);
                break;
            case 12:
                quotient = (long) Math.floor(nb / 1000000000);
                reste = nb - quotient * 1000000000;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "un milliard";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "un milliard" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " milliards";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " milliards " + numberToLetter(reste);
                break;
            case 13:
                quotient = (long) Math.floor(nb / 1000000000000L);
                reste = nb - quotient * 1000000000000L;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "un billion";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "un billion" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " billions";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " billions " + numberToLetter(reste);
                break;
            case 14:
                quotient = (long) Math.floor(nb / 1000000000000L);
                reste = nb - quotient * 1000000000000L;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "un billion";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "un billion" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " billions";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " billions " + numberToLetter(reste);
                break;
            case 15:
                quotient = (long) Math.floor(nb / 1000000000000L);
                reste = nb - quotient * 1000000000000L;
                if (quotient == 1 && reste == 0)
                    numberToLetter = "un billion";
                if (quotient == 1 && reste != 0)
                    numberToLetter = "un billion" + " " + numberToLetter(reste);
                if (quotient > 1 && reste == 0)
                    numberToLetter = numberToLetter(quotient) + " billions";
                if (quotient > 1 && reste != 0)
                    numberToLetter = numberToLetter(quotient) + " billions " + numberToLetter(reste);
                break;
        }
        // respect de l'accord de quatre-vingt
        final String QUATRE_VINGT = "quatre-vingt";
        if (numberToLetter.endsWith(QUATRE_VINGT))
            numberToLetter += "s";
        return numberToLetter;
    }// -----------------------------------------------------------------------
}
