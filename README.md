NIOStudy

this project is used to study NIO/AIO and compare java Blocking IO, NIO and AIO performance. 

The main idea is create a server to receive request and many clients to send request, then record how many requests the server can handle in specify time. I use 50 threads to simulate the clients, every client continuous send request and wait response in 5ms interval, and the server run 10s to receive the request and send response.

The result is BIO can handle around 2000 requests per second, NIO can handle 7500 requests per second and also NIO can handle 7500 requests per second.
1. The BIO data is not stable, I run BIO code several time and get 2500/1854/2321/2007/1697/2787, I think in BIO every client connection will create a server thread to handle so which will create many threads, cpu will cost much time to switch threads, so I think that's why NIO result is not stable.
2. NIO and AIO are 7500 tps, because in linux they are implemented by epoll, so they can get same result. The reason why BIO low performance is because the connection threads don't know when to read and when to write, they always blocks until data are copying to username space(or writting to kernel).


this project contains 4 packages, the com.eason.bio package contains Blocking IO source code, the com.eason.nio package contains the NIO code, then com.eason.aio contains the AIO code, the com.eason.aio.test package is test code.
