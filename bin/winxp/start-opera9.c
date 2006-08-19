/**
 *  Replace opera6.ini with a clean version and start a detached Opera9
 *
 *  Compile under cygwin via
 *    gcc -o start-opera9 start-opera9.c
 *
 *  This requires a clean opera6.ini file copied to opera6.ini.clean
 *    in the opera profile folder, where "clean" means that under
 *    [Sate], Run=0. This prevents a dialog on browser startup.
 *
 *  author: dws@pivotalsf.com
 */

#include <stdlib.h>

char *opera_profile = "/Application Data/Opera/Opera/profile/";

main(int argc, char *argv[]) {
  int pid;

  if ((pid = fork()) < 0) {
    /* fork failed */
    exit(1);
  } else if (pid == 0) {
    /* start with a clean .ini */

    char userprofile[512] = "";
    strcat(userprofile, getenv("USERPROFILE"));
    char *p;
    for ( p = userprofile ; *p != 0 ; p++ ) {
      if ( *p == '\\' ) *p = '/';
    }

    char clean_ini[512] = "";
    strcat(clean_ini, userprofile);
    strcat(clean_ini, opera_profile);
    strcat(clean_ini, "opera6.ini.clean");
   
    char opera_ini[512] = "";
    strcat(opera_ini, userprofile);
    strcat(opera_ini, opera_profile);
    strcat(opera_ini, "opera6.ini");

    execl(
      "c:/cygwin/bin/cp.exe",
      "cp",
      clean_ini,
      opera_ini,
      (char *) 0
    );
    exit(1);
  } else {
    /* wait for child to exit */
    int status;
    while (wait(&status) != pid)
      /* wait more */;
  }
  

  /* start a detached opera */
  if ((pid = fork()) == 0 ) {
    execl(
      "c:/program files/opera/opera.exe",
      "opera",
      argv[1],
      (char *) 0
    );
  }
  exit(pid > 0 ? 0 : 1);
}
