using namespace std;
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <unistd.h>
#include "imdb.h"
#include <string.h>


const char *const imdb::kActorFileName = "actordata";
const char *const imdb::kMovieFileName = "moviedata";

imdb::imdb(const string& directory)
{
  const string actorFileName = directory + "/" + kActorFileName;
  const string movieFileName = directory + "/" + kMovieFileName;
  
  actorFile = acquireFileMap(actorFileName, actorInfo);
  movieFile = acquireFileMap(movieFileName, movieInfo);
}

bool imdb::good() const
{
  return !( (actorInfo.fd == -1) || 
	    (movieInfo.fd == -1) ); 
}

int compare(const void* a, const void* b){
  pair<void*,void*>* pr = (pair<void*,void*>*) a;
  void* base = pr->second;
  char* toCompare = (char*)base + *(int*)b;
  char* key = (char*)(pr->first);

  return strcmp(key, toCompare);
}

// you should be implementing these two methods right here... 
bool imdb::getCredits(const string& player, vector<film>& films) const { 
  //key, base, num, size, compar

  
  pair<void*, void*> key; //string, file
  key.first = (void*)player.c_str();
  key.second = (void*)actorFile;
  void* base = (int*)actorFile + 1;
  int num = *(int*)actorFile;
  int size = sizeof(int);

  void* bnrSrch = bsearch(&key, base, num, size, compare);
  if(!bnrSrch) return false;

  void* curActorFile = (char*)actorFile + *(int*)bnrSrch;

  char* iter;
  int curBytes = 0;

  player.length() % 2 == 0? iter = (char*)curActorFile + player.length() + 2
      : iter = (char*)curActorFile + player.length() + 1;

  player.length() % 2 == 0? curBytes = player.length() + 4: curBytes = player.length() + 3;
  short numOfMovies = *(short*)iter;
  iter+=2; 

  if(curBytes % 4 != 0) iter += 2;
  
  for(int i = 0; i < numOfMovies; i++){
    film filmToAdd;

    int offset = *(int*)iter;
    iter+=4;
    char* movieTitle = (char*)movieFile + offset;

    string res = "";
    while(*movieTitle != '\0'){
      res+=*movieTitle;
      movieTitle++;
    }
    movieTitle++;

    int releaseDate = 1900 + *movieTitle;

    filmToAdd.title = res;
    filmToAdd.year = releaseDate;
    films.push_back(filmToAdd);
  }

  return true; 
  }

int compareFilm(const void* a, const void* b){
  pair<void*,void*>* pr = (pair<void*,void*>*)a;
  void* movieFile = pr->second;
  film* movie = (film*)(pr->first);

  string filmTitle;
  char* filmToCompare = (char*)movieFile + *(int*)b;

  while(*filmToCompare!= '\0'){
    filmTitle += *filmToCompare;
    filmToCompare++;
  }
  filmToCompare++;

  int releaseDate = 1900 + *filmToCompare;

  film filmToCmp;
  filmToCmp.title = filmTitle;
  filmToCmp.year = releaseDate;

  if(*movie < filmToCmp) return -1;
  if(*movie == filmToCmp) return 0;
  return 1;
}

bool imdb::getCast(const film& movie, vector<string>& players) const { 
  //key, base, num, size, compar

  pair<void*, void*> key; //string, file
  key.first = (void*)&movie;
  key.second = (void*)movieFile;
  void* base = (int*)movieFile + 1;
  int num = *(int*)movieFile;
  int size = sizeof(int);

  void* binarySrch = bsearch(&key,base,num,size,compareFilm);

  if(!binarySrch) return false;

  void* curMovieFile = (char*)movieFile + *(int*)binarySrch;

  char* iter = (char*)curMovieFile + movie.title.length() + 1;
  int curBytes = movie.title.length() + 2;
  int year = *(char*)iter;
  iter++;
  if(curBytes%2 != 0) {
    iter++;
    curBytes++;
  }
  short numActors = *(short*)iter;
  iter+=2;
  curBytes+=2;
  if(curBytes%4 != 0) iter+=2;

  for(int i = 0; i < numActors; i++){
    string actor;

    char* curActorFile = (char*)actorFile + *(int*)iter;
    iter += 4;

    while(*curActorFile != '\0'){
      actor+=*curActorFile;
      curActorFile++;
    }

    players.push_back(actor);
  }

  return true; 
 }

imdb::~imdb()
{
  releaseFileMap(actorInfo);
  releaseFileMap(movieInfo);
}

// ignore everything below... it's all UNIXy stuff in place to make a file look like
// an array of bytes in RAM.. 
const void *imdb::acquireFileMap(const string& fileName, struct fileInfo& info)
{
  struct stat stats;
  stat(fileName.c_str(), &stats);
  info.fileSize = stats.st_size;
  info.fd = open(fileName.c_str(), O_RDONLY);
  return info.fileMap = mmap(0, info.fileSize, PROT_READ, MAP_SHARED, info.fd, 0);
}

void imdb::releaseFileMap(struct fileInfo& info)
{
  if (info.fileMap != NULL) munmap((char *) info.fileMap, info.fileSize);
  if (info.fd != -1) close(info.fd);
}
